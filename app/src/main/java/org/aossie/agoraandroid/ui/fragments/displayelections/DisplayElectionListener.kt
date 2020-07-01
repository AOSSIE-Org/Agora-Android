package org.aossie.agoraandroid.ui.fragments.displayelections

interface DisplayElectionListener {
  fun onDeleteElectionSuccess()
  fun onSuccess(message: String?= null)
  fun onStarted()
  fun onFailure(message: String)
}