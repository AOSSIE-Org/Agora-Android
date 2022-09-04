package org.aossie.agoraandroid.domain.useCases.authentication.login

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.LoginDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class UserLogInUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  private val authResponseMapper = AuthResponseEntityMapper()
  suspend operator fun invoke(
    loginDtoModel: LoginDtoModel
  ): AuthResponseModel {
    val response = repository.userLogin(mappers.loginDtoEntityMapper.mapToEntity(loginDtoModel))
    return mappers.authResponseEntityMapper.mapFromEntity(response)
  }
}
