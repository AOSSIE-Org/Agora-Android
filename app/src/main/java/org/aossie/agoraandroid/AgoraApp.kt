package org.aossie.agoraandroid

import android.app.Application
import org.aossie.agoraandroid.di.AppComponent
import org.aossie.agoraandroid.di.DaggerAppComponent

class AgoraApp : Application() {

  val appComponent: AppComponent by lazy {
    DaggerAppComponent.factory()
        .create(applicationContext)
  }
}