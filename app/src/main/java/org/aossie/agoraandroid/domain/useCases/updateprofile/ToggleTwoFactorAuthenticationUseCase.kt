package org.aossie.agoraandroid.domain.useCases.updateprofile

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class ToggleTwoFactorAuthenticationUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke() : List<String> {
      return repository.toggleTwoFactorAuth()
  }
}