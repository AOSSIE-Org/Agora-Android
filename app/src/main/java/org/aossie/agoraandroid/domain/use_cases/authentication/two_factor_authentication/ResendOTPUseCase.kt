package org.aossie.agoraandroid.domain.use_cases.authentication.two_factor_authentication

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class ResendOTPUseCase @Inject constructor(
  private val repository: UserRepository
) {
  private val authResponseEntityMapper = AuthResponseEntityMapper()
  suspend operator fun invoke(
    username: String
  ): AuthResponseModel {
    return authResponseEntityMapper.mapFromEntity(repository.resendOTP(username))
  }
}
