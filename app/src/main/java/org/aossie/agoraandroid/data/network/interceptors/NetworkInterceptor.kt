package org.aossie.agoraandroid.data.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.common.utilities.InternetManager
import org.aossie.agoraandroid.common.utilities.NoInternetException
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkInterceptor
@Inject
constructor(
  context: Context,
  val internetManager: InternetManager
) : Interceptor {

  private val applicationContext = context.applicationContext
  override fun intercept(chain: Interceptor.Chain): Response {
    if (!internetManager.isConnected())
      throw NoInternetException(applicationContext.resources.getString(R.string.no_network))
    return chain.proceed(chain.request())
  }
}
