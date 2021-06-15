package org.aossie.agoraandroid.ui.fragments.auth.login

interface LoginListener {
  fun onTwoFactorAuthentication(crypto: String)
}
