package org.aossie.agoraandroid.utilities

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.core.content.ContextCompat.getSystemService

object CheckNetwork {
    //Function that returns whether the phone is connected to internet or not
    fun isNetworkConnected(context: Context): Boolean {
        val cm =context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm != null) {
            val activeNetwork:NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }
        return false
    }
}