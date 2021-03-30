package org.aossie.agoraandroid.ui.fragments.electionDetails

interface DisplayElectionListener {
  fun onDeleteElectionSuccess()
  fun onSuccess(message: String? = null)
  fun onStarted()
  fun onFailure(message: String)
}
