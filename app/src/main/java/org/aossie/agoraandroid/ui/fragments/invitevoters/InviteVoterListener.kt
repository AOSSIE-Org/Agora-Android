package org.aossie.agoraandroid.ui.fragments.invitevoters

interface InviteVoterListener {
  fun onStarted()
  fun onFailure(message: String)
  fun onSuccess(message: String)
}
