package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class UserLogInUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  private val authResponseMapper = AuthResponseEntityMapper()
  suspend operator fun invoke(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ): AuthResponseModel {
    val response =  repository.userLogin(LoginDto(identifier, trustedDevice, password))
    return authResponseMapper.mapFromEntity(response)
  }
}

