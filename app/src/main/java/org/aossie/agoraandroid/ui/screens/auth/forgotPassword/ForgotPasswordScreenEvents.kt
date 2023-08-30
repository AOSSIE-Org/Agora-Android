package org.aossie.agoraandroid.ui.screens.auth.forgotPassword

sealed class ForgotPasswordScreenEvents{
  object OnBackIconClick:ForgotPasswordScreenEvents()
  object OnSendLinkClick:ForgotPasswordScreenEvents()
  object SnackBarActionClick:ForgotPasswordScreenEvents()
  data class OnUserNameEntered(val userName:String):ForgotPasswordScreenEvents()
}