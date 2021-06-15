package org.aossie.agoraandroid.data.network.interceptors

import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Named

class AuthorizationInterceptor(
  private val prefs: PreferenceProvider,
  private val appDatabase: AppDatabase,
  @Named("apiWithoutAuth") private val api: Api
) : Interceptor, ApiRequest() {

  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request()
    val mainResponse = chain.proceed(request)
    var tryCount = AppConstants.retryCount

    // if response code is 401, network call has encountered authentication error
    while (mainResponse.code == AppConstants.UNAUTHENTICATED_CODE) {
      if (prefs.getIsLoggedIn()) {
        runBlocking {
          if (tryCount == 0) {
            throw SessionExpirationException()
          }
          tryCount--
          val newToken = renewTokenAndUpdateUser()
          prefs.setAccessToken(newToken)
        }
        mainResponse.close()
        return chain.proceed(updateRequestWithToken(request))
      } else {
        throw SessionExpirationException()
      }
    }
    return mainResponse
  }

  private suspend fun renewTokenAndUpdateUser(): String? {
    var user = appDatabase.getUserDao()
      .getUserInfo()
    if (prefs.getIsFacebookUser()) {
      val response = api.facebookLogin()
      if (response.isSuccessful) {
        // save new access token
        prefs.setAccessToken(response.body()?.authToken?.token)
        user.authToken = response.body()?.authToken?.token
        user.authTokenExpiresOn = response.body()?.authToken?.expiresOn
      } else {
        throw SessionExpirationException()
      }
    } else {
      val loginResponse = api.refreshAccessToken()
      if (loginResponse.isSuccessful) {
        val authResponse: AuthResponse? = loginResponse.body()
        authResponse.let {
          user = User(
            it?.username, it?.email, it?.firstName, it?.lastName, it?.avatarURL,
            it?.crypto, it?.twoFactorAuthentication,
            it?.authToken?.token, it?.authToken?.expiresOn, it?.refreshToken?.token,
            it?.refreshToken?.expiresOn, user.trustedDevice
          )
          Timber.d(authResponse.toString())
        }
      } else {
        throw SessionExpirationException()
      }
    }
    appDatabase.getUserDao()
      .replace(user)
    return user.authToken
  }

  private fun updateRequestWithToken(request: Request): Request {
    return request.newBuilder()
      .header(AppConstants.X_AUTH_TOKEN, prefs.getAccessToken() ?: "")
      .build()
  }
}
