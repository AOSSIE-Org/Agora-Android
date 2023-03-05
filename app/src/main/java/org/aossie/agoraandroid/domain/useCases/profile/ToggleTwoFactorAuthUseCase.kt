package org.aossie.agoraandroid.domain.useCases.profile

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class ToggleTwoFactorAuthUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke(): List<String> {
    return repository.toggleTwoFactorAuth()
  }
}
