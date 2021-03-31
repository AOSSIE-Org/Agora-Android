package org.aossie.agoraandroid

import android.app.Application
import android.util.Log
import org.aossie.agoraandroid.di.AppComponent
import org.aossie.agoraandroid.di.DaggerAppComponent
import org.aossie.agoraandroid.utilities.ConnectivityManager
import timber.log.Timber
import timber.log.Timber.DebugTree
import timber.log.Timber.Tree

class AgoraApp : Application() {
  private lateinit var connectivityManager: ConnectivityManager
  val appComponent: AppComponent by lazy {
    DaggerAppComponent.factory()
      .create(applicationContext)
  }

  override fun onCreate() {
    super.onCreate()
    Timber.plant(if (BuildConfig.DEBUG) DebugTree() else CrashReportingTree())
    connectivityManager = ConnectivityManager(this)
    connectivityManager.observeForever {
      IS_NETWORK_CONNECTED = it
    }
  }

  private class CrashReportingTree : Tree() {

    override fun isLoggable(tag: String?, priority: Int): Boolean {
      return priority >= Log.ERROR
    }
    // Log Exception to Crashlytics here
    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
    }
  }

  companion object {
    var IS_NETWORK_CONNECTED = false
  }
}
