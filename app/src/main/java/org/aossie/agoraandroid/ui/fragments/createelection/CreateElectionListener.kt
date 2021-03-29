package org.aossie.agoraandroid.ui.fragments.createelection

interface CreateElectionListener {

  fun onStarted()
  fun onSuccess(message: String?)
  fun onFailure(message: String)
  fun onSessionExpired()
}