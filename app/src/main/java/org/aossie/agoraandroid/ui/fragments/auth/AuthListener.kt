package org.aossie.agoraandroid.ui.fragments.auth

import org.aossie.agoraandroid.data.db.entities.User

interface AuthListener {
  fun onSuccess()
  fun onStarted()
  fun onFailure(message:  String)
}