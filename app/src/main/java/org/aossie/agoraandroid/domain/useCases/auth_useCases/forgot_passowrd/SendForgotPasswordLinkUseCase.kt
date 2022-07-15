package org.aossie.agoraandroid.domain.useCases.auth_useCases.forgot_passowrd

import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SendForgotPasswordLinkUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke(
    username : String?
  ) : String {
    return repository.sendForgotPasswordLink(username)
  }
}