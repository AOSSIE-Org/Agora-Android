package org.aossie.agoraandroid.ui.screens.auth.signup.events

sealed class SignUpViewModelEvent{
  data class EnteredUsername(val username:String): SignUpViewModelEvent()
  data class EnteredFirstName(val firstName:String): SignUpViewModelEvent()
  data class EnteredLastName(val lastName:String): SignUpViewModelEvent()
  data class EnteredEmail(val email:String): SignUpViewModelEvent()
  data class EnteredPassword(val password:String): SignUpViewModelEvent()
  data class SelectedSecurityQuestion(val securityQuestion:String): SignUpViewModelEvent()
  data class EnteredSecurityAnswer(val securityAnswer:String): SignUpViewModelEvent()
  object SignUpClick: SignUpViewModelEvent()
}