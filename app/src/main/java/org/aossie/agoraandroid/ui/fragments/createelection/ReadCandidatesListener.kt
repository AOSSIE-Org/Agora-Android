package org.aossie.agoraandroid.ui.fragments.createelection

interface ReadCandidatesListener {

  fun onReadSuccess(list: ArrayList<String>)
  fun onReadFailure(message: String)
}
