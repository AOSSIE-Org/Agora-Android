package org.aossie.agoraandroid.domain.use_cases.profile

import org.aossie.agoraandroid.domain.use_cases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.SaveUserUseCase

data class ProfileUseCases(
  val saveUserUseCase: SaveUserUseCase,
  val changePasswordUseCase: ChangePasswordUseCase,
  val changeAvatarUseCase: ChangeAvatarUseCase,
  val getUserUseCase: GetUserUseCase,
  val toggleTwoFactorAuthUseCase: ToggleTwoFactorAuthUseCase,
  val updateUserUseCase: UpdateUserUseCase,
  val getUserDataUseCase: GetUserDataUseCase
)
