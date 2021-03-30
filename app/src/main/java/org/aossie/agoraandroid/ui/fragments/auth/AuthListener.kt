package org.aossie.agoraandroid.ui.fragments.auth

interface AuthListener {
  fun onSuccess(message: String? = null)
  fun onStarted()
  fun onFailure(message: String)
}
