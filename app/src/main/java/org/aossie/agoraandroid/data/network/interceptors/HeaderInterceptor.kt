package org.aossie.agoraandroid.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.utilities.AppConstants

class HeaderInterceptor(private val preferenceProvider: PreferenceProvider) : Interceptor {
  override fun intercept(chain: Chain): Response {

    val request = chain.request()
      .newBuilder()
      .apply {
        when {
          chain.request().url.toString()
            .contains(AppConstants.FACEBOOK) -> addHeader(
            AppConstants.ACCESS_TOKEN, preferenceProvider.getFacebookAccessToken() ?: ""
          )
          chain.request().url.toString()
            .contains(AppConstants.REFRESH_ACCESS_TOKEN) -> addHeader(
            AppConstants.X_REFRESH_TOKEN, preferenceProvider.getRefreshToken() ?: ""
          )
          else -> addHeader(AppConstants.X_AUTH_TOKEN, preferenceProvider.getAccessToken() ?: "")
        }

        addHeader(AppConstants.ACCEPT, AppConstants.APPLICATION_JSON)
        addHeader(AppConstants.CONTENT_TYPE, AppConstants.APPLICATION_JSON)
      }
      .build()
    return chain.proceed(request)
  }
}
