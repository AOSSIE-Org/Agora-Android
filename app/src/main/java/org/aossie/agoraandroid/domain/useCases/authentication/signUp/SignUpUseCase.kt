package org.aossie.agoraandroid.domain.useCases.authentication.signUp

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    newUserDtoModel: NewUserDtoModel
  ): String {
    return repository.userSignup(mappers.newUserDtoEntityMapper.mapToEntity(newUserDtoModel))
  }
}
