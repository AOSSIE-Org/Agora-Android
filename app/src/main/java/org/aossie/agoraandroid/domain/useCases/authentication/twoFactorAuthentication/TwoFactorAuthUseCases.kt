package org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication

import org.aossie.agoraandroid.domain.useCases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.SaveUserUseCase

data class TwoFactorAuthUseCases(
  val resendOTPUseCase: ResendOTPUseCase,
  val verifyOTPUseCase: VerifyOTPUseCase,
  val saveUserUseCase: SaveUserUseCase,
  val getUserUseCase: GetUserUseCase
)
