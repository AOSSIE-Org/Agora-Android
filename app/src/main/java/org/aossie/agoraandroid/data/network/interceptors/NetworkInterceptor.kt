package org.aossie.agoraandroid.data.network.interceptors

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.isConnected
import javax.inject.Inject

@Suppress("DEPRECATION")
class NetworkInterceptor
@Inject
constructor(
  context: Context
) : Interceptor {

  private val applicationContext = context.applicationContext
  override fun intercept(chain: Interceptor.Chain): Response {
    if (!applicationContext.isConnected())
      throw NoInternetException(applicationContext.resources.getString(R.string.no_network))
    return chain.proceed(chain.request())
  }
}
