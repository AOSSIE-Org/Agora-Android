package org.aossie.agoraandroid.ui.screens.auth.signup.events

sealed class SignUpScreenEvent{
  data class EnteredUsername(val username:String): SignUpScreenEvent()
  data class EnteredFirstName(val firstName:String): SignUpScreenEvent()
  data class EnteredLastName(val lastName:String): SignUpScreenEvent()
  data class EnteredEmail(val email:String): SignUpScreenEvent()
  data class EnteredPassword(val password:String): SignUpScreenEvent()
  data class SelectedSecurityQuestion(val question:String): SignUpScreenEvent()
  data class EnteredSecurityAnswer(val answer:String): SignUpScreenEvent()
  object BackArrowClick: SignUpScreenEvent()
  object SnackBarActionClick: SignUpScreenEvent()
  object SignUpClICK: SignUpScreenEvent()

}
