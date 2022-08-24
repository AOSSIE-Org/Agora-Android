package org.aossie.agoraandroid.domain.use_cases.authentication.two_factor_authentication

import org.aossie.agoraandroid.domain.use_cases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.SaveUserUseCase

data class TwoFactorAuthUseCases(
  val resendOTPUseCase: ResendOTPUseCase,
  val verifyOTPUseCase: VerifyOTPUseCase,
  val saveUserUseCase: SaveUserUseCase,
  val getUserUseCase: GetUserUseCase
)
