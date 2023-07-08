package org.aossie.agoraandroid.ui.screens.auth.login.events

sealed class LoginViewModelEvent{
  data class EnteredUserName(val username:String): LoginViewModelEvent()
  data class EnteredPassword(val password:String): LoginViewModelEvent()
  object LoginClick: LoginViewModelEvent()
}
