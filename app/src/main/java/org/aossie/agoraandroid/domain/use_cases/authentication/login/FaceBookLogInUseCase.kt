package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class FaceBookLogInUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
  ) : AuthResponse {
    return repository.fbLogin()
  }
}