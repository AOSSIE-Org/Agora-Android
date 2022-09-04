package org.aossie.agoraandroid.domain.useCases.profile

import org.aossie.agoraandroid.domain.useCases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.authentication.login.SaveUserUseCase

data class ProfileUseCases(
  val saveUser: SaveUserUseCase,
  val changePassword: ChangePasswordUseCase,
  val changeAvatar: ChangeAvatarUseCase,
  val getUser: GetUserUseCase,
  val toggleTwoFactorAuth: ToggleTwoFactorAuthUseCase,
  val updateUser: UpdateUserUseCase,
  val getUserData: GetUserDataUseCase
)
