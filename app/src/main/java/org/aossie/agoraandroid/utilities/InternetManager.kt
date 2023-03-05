package org.aossie.agoraandroid.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_MOBILE
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.TRANSPORT_CELLULAR
import android.net.NetworkCapabilities.TRANSPORT_ETHERNET
import android.net.NetworkCapabilities.TRANSPORT_WIFI
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

sealed class NetworkStatus {
  object Available : NetworkStatus()
  object Unavailable : NetworkStatus()
}
@ExperimentalCoroutinesApi
class InternetManager(context: Context) {

  private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  fun isConnected(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val activeNetwork = connectivityManager.activeNetwork ?: return false
      val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
      return when {
        capabilities.hasTransport(TRANSPORT_WIFI) -> true
        capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
        capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
        else -> false
      }
    } else {
      connectivityManager.activeNetworkInfo?.run {
        return when (type) {
          TYPE_WIFI -> true
          TYPE_MOBILE -> true
          TYPE_ETHERNET -> true
          else -> false
        }
      }
    }
    return false
  }

  val networkStatus = callbackFlow<NetworkStatus> {
    val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
      override fun onUnavailable() {
        trySend(NetworkStatus.Unavailable)
      }

      override fun onAvailable(network: Network) {
        trySend(NetworkStatus.Available)
      }

      override fun onLost(network: Network) {
        trySend(NetworkStatus.Unavailable)
      }
    }

    val request = NetworkRequest.Builder()
      .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .build()
    connectivityManager.registerNetworkCallback(request, networkStatusCallback)

    awaitClose {
      connectivityManager.unregisterNetworkCallback(networkStatusCallback)
    }
  }
}
