package org.aossie.agoraandroid.utilities

import android.content.Context
import android.net.ConnectivityManager

fun Context.isConnected(): Boolean {
  val connectivityManager =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  connectivityManager.activeNetworkInfo.also {
    return it != null && it.isConnected
  }
}