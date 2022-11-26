package org.aossie.agoraandroid.domain.useCases.authentication.login

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    user: UserModel
  ) {
    return repository.saveUser(mappers.userEntityMapper.mapToEntity(user))
  }
}
