package org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class ResendOTPUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    username: String
  ): AuthResponseModel {
    return mappers.authResponseEntityMapper.mapFromEntity(repository.resendOTP(username))
  }
}
