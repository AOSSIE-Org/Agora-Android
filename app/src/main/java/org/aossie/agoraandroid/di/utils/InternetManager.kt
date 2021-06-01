package org.aossie.agoraandroid.di.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
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

  val networkStatus = callbackFlow<NetworkStatus> {
    val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
      override fun onUnavailable() {
        offer(NetworkStatus.Unavailable)
      }

      override fun onAvailable(network: Network) {
        offer(NetworkStatus.Available)
      }

      override fun onLost(network: Network) {
        offer(NetworkStatus.Unavailable)
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
