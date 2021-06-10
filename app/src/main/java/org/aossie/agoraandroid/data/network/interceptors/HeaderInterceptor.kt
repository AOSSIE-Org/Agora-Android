package org.aossie.agoraandroid.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response
import org.aossie.agoraandroid.data.db.PreferenceProvider

class HeaderInterceptor(private val preferenceProvider: PreferenceProvider) : Interceptor {
  override fun intercept(chain: Chain): Response {
    val request = chain.request().newBuilder()
      .addHeader("X-Auth-Token", preferenceProvider.getCurrentToken() ?: "")
      .addHeader("Accept", "application/json")
      .addHeader("Content-Type", "application/json")
      .build()

    return chain.proceed(request)
  }
}
