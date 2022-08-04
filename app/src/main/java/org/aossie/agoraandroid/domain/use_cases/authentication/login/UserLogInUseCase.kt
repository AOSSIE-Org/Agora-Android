package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class UserLogInUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ): AuthResponse {
    return repository.userLogin(LoginDto(identifier, password, trustedDevice))
  }
}

