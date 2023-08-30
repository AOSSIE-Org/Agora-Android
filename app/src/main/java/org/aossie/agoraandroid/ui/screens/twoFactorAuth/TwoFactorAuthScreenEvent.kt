package org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth

sealed class TwoFactorAuthScreenEvent {
  object OnBackClick: TwoFactorAuthScreenEvent()
  object ResendOtpClick: TwoFactorAuthScreenEvent()
  data class VerifyOtpClick(val otp: String, val trustedDevice:Boolean): TwoFactorAuthScreenEvent()
}
