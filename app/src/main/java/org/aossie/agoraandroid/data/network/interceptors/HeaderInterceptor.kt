package org.aossie.agoraandroid.data.network.interceptors

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.common.utilities.AppConstants

class HeaderInterceptor(private val preferenceProvider: PreferenceProvider, private val serverKey: String) : Interceptor {
  override fun intercept(chain: Chain): Response {
    val request = runBlocking {
      chain.request()
        .newBuilder()
        .apply {
          when {
            chain.request().url.toString().contains(AppConstants.FCM_URL) -> addHeader(
              AppConstants.AUTHORIZATION,
              AppConstants.KEY + serverKey
            )
            chain.request().url.toString()
              .contains(AppConstants.FACEBOOK) -> addHeader(
              AppConstants.ACCESS_TOKEN,
              preferenceProvider.getFacebookAccessToken()
                .first() ?: ""
            )
            chain.request().url.toString()
              .contains(AppConstants.REFRESH_ACCESS_TOKEN) -> addHeader(
              AppConstants.X_REFRESH_TOKEN,
              preferenceProvider.getRefreshToken()
                .first() ?: ""
            )
            else -> addHeader(
              AppConstants.X_AUTH_TOKEN,
              preferenceProvider.getAccessToken()
                .first() ?: ""
            )
          }
          addHeader(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
          addHeader(AppConstants.CONTENT_TYPE, AppConstants.APPLICATION_JSON)
        }
    }.build()
    return chain.proceed(request)
  }
}
