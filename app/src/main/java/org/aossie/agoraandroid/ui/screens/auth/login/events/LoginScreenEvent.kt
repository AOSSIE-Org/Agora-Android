package org.aossie.agoraandroid.ui.screens.auth.login.events

sealed class LoginScreenEvent{
  data class EnteredUsername(val username:String): LoginScreenEvent()
  data class EnteredPassword(val password:String): LoginScreenEvent()
  object BackArrowClick: LoginScreenEvent()
  object LoginClick: LoginScreenEvent()
  object LoginFacebookClick: LoginScreenEvent()
  object ForgotPasswordClick: LoginScreenEvent()

}
