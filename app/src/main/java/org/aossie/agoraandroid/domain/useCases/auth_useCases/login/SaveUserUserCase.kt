package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  suspend operator fun invoke(
    user : User
  ) : Unit{
    return repository.saveUser(user)
  }
}

