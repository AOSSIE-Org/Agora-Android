package org.aossie.agoraandroid.domain.useCases.profile

import org.aossie.agoraandroid.domain.useCases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.SaveUserUseCase

data class ProfileUseCases(
  val saveUserUseCase: SaveUserUseCase,
  val changePasswordUseCase: ChangePasswordUseCase,
  val changeAvatarUseCase: ChangeAvatarUseCase,
  val getUserUseCase: GetUserUseCase,
  val toggleTwoFactorAuthUseCase: ToggleTwoFactorAuthUseCase,
  val updateUserUseCase: UpdateUserUseCase,
  val getUserDataUseCase: GetUserDataUseCase
)
