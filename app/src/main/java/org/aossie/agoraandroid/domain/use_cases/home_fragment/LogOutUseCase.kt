package org.aossie.agoraandroid.domain.use_cases.home_fragment

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke() {
    return userRepository.deleteUser()
  }
}
