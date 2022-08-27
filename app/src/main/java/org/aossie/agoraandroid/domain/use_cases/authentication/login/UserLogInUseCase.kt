package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.data.mappers.LogInDtoEntityMapper
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.LoginDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class UserLogInUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  private val authResponseMapper = AuthResponseEntityMapper()
  private val logInDtoEntityMapper = LogInDtoEntityMapper()
  suspend operator fun invoke(
    loginDtoModel: LoginDtoModel
  ): AuthResponseModel {
    val response = repository.userLogin(logInDtoEntityMapper.mapToEntity(loginDtoModel))
    return authResponseMapper.mapFromEntity(response)
  }
}
