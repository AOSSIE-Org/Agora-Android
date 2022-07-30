package org.aossie.agoraandroid.data.network.interceptors

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.apiservice.Api
import org.aossie.agoraandroid.data.remote.apiservice.ApiRequest
import org.aossie.agoraandroid.data.remote.models.AuthResponse
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

    // if response code is 401, network call has encountered authentication error
    if (mainResponse.code == AppConstants.UNAUTHENTICATED_CODE) {
      return runBlocking {
        if (prefs.getIsLoggedIn().first()) {
          val newToken = renewTokenAndUpdateUser()
          prefs.setAccessToken(newToken)
          mainResponse.close()
          chain.proceed(updateRequestWithToken(request))
        } else {
          throw SessionExpirationException()
        }
      }
    }
    return mainResponse
  }

  private suspend fun renewTokenAndUpdateUser(): String? {
    val refreshAccessResponse = api.refreshAccessToken()
    if (refreshAccessResponse.isSuccessful) {
      val authResponse: AuthResponse? = refreshAccessResponse.body()
      authResponse.let {
        val user = User(
          it?.username, it?.email, it?.firstName, it?.lastName, it?.avatarURL,
          it?.crypto, it?.twoFactorAuthentication,
          it?.authToken?.token, it?.authToken?.expiresOn, it?.refreshToken?.token,
          it?.refreshToken?.expiresOn, it?.trustedDevice
        )
        appDatabase.getUserDao()
          .replace(user)
        Timber.d(authResponse.toString())
        return user.authToken
      }
    } else {
      throw SessionExpirationException()
    }
  }

  private suspend fun updateRequestWithToken(request: Request): Request {
    return request.newBuilder()
      .header(AppConstants.X_AUTH_TOKEN, prefs.getAccessToken().first() ?: "")
      .build()
  }
}
