package org.aossie.agoraandroid.ui.activities.castVote

interface GetResolvedPathListener {

  fun onStarted()
  fun onSuccess(message: String)
  fun onFailure(message: String? = null)
}
