package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class FaceBookLogInUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke() : AuthResponse{
    return repository.fbLogin()
  }
}