package org.aossie.agoraandroid.ui.screens.auth.login.events

sealed class LoginUiEvent {
  object UserLoggedIn: LoginUiEvent()
  data class OnTwoFactorAuthentication(val crypto: String): LoginUiEvent()

}