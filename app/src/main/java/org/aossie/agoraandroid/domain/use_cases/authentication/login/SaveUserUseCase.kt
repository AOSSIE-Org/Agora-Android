package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.mappers.UserEntityMapper
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  private val mapper: UserEntityMapper = UserEntityMapper()
  suspend operator fun invoke(
    user: UserModel
  ) {
    return repository.saveUser(mapper.mapToEntity(user))
  }
}
