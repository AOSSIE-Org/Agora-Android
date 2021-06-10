
package org.aossie.agoraandroid.data.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.dto.LoginDto
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Named

class AuthorizationInterceptor(
  private val context: Context,
  private val prefs: PreferenceProvider,
  private val appDatabase: AppDatabase,
  @Named("apiWithoutAuth") private val api: Api
) : Interceptor, ApiRequest() {

  override fun intercept(chain: Interceptor.Chain): Response {
    val mainResponse = chain.proceed(chain.request())

    // if response code is 401 or 403, network call has encountered authentication error
    if (mainResponse.code == AppConstants.UNAUTHENTICATED_CODE || mainResponse.code == AppConstants.INVALID_CREDENTIALS_CODE) {
      if (prefs.getIsLoggedIn()) {
        Coroutines.io {
          var user = appDatabase.getUserDao().getUserInfo()
          if (prefs.getIsFacebookUser()) {
            val response = api.facebookLogin()
            if (response.isSuccessful) {
              // save new access token
              prefs.setCurrentToken(response.body()!!.authToken?.token)
              user.token = response.body()!!.authToken?.token
              user.expiredAt = response.body()!!.authToken?.expiresOn
            } else {
              prefs.setIsLoggedIn(false)
              throw SessionExpirationException(context.resources.getString(R.string.token_expired))
            }
          } else {

            val loginResponse = api.logIn(LoginDto(user.username ?: "", user.trustedDevice ?: "", user.password ?: ""))
            if (loginResponse.isSuccessful) {
              val authResponse: AuthResponse? = loginResponse.body()
              authResponse.let {
                user = User(
                  it?.username, it?.email, it?.firstName, it?.lastName, it?.avatarURL,
                  it?.crypto, it?.twoFactorAuthentication,
                  it?.authToken?.token, it?.authToken?.expiresOn, user.password, user.trustedDevice
                )
                Timber.d(authResponse.toString())
              }
            } else {
              prefs.setIsLoggedIn(false)
              throw SessionExpirationException(context.resources.getString(R.string.token_expired))
            }
          }
          appDatabase.getUserDao().replace(user)
          prefs.setCurrentToken(user.token)
        }
      }
    }
    return mainResponse
  }
}
