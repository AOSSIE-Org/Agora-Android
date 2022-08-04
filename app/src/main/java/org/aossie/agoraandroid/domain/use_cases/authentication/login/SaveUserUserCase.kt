package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    user : User
  ) {
    return repository.saveUser(user)
  }
}

