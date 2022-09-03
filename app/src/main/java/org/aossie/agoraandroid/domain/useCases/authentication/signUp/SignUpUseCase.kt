package org.aossie.agoraandroid.domain.useCases.authentication.signUp

import org.aossie.agoraandroid.data.mappers.NewUserDtoEntityMapper
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
  private val repository: UserRepository
) {
  private val mapper = NewUserDtoEntityMapper()
  suspend operator fun invoke(
    newUserDtoModel: NewUserDtoModel
  ): String {
    return repository.userSignup(mapper.mapToEntity(newUserDtoModel))
  }
}
