package org.aossie.agoraandroid.ui.screens.auth.models

data class SignUpModel(
  val username: String = "",
  val firstName: String = "",
  val lastName: String = "",
  val email: String = "",
  val password: String = "",
  val securityQuestion: String = "",
  val securityAnswer: String = "",
)
