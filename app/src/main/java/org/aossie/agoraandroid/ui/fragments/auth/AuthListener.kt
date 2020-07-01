package org.aossie.agoraandroid.ui.fragments.auth

import org.aossie.agoraandroid.data.db.entities.User

interface AuthListener {
  fun onSuccess(message: String?= null)
  fun onStarted()
  fun onFailure(message:  String)
}