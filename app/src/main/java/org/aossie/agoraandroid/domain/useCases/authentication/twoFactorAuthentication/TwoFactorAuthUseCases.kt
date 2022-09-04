package org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication

import org.aossie.agoraandroid.domain.useCases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.SaveUserUseCase

data class TwoFactorAuthUseCases(
  val resendOTP: ResendOTPUseCase,
  val verifyOTP: VerifyOTPUseCase,
  val saveUser: SaveUserUseCase,
  val getUser: GetUserUseCase
)
