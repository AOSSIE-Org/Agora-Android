package org.aossie.agoraandroid.domain.useCases.homeFragment

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke() {
    return userRepository.logout()
  }
}
