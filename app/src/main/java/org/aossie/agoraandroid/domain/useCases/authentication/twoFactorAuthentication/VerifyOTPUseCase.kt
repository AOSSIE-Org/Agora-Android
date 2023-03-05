package org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.VerifyOtpDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    verifyOtpDtoModel: VerifyOtpDtoModel
  ): AuthResponseModel {
    return mappers.authResponseEntityMapper.mapFromEntity(
      repository.verifyOTP(
        mappers.verifyOtpDtoEntityMapper.mapToEntity(
          verifyOtpDtoModel
        )
      )
    )
  }
}
