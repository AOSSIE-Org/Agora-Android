package org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.data.mappers.VerifyOtpDtoEntityMapper
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.VerifyOtpDtoModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class VerifyOTPUseCase @Inject constructor(
  private val repository: UserRepository
) {
  private val verifyOtpDtoEntityMapper = VerifyOtpDtoEntityMapper()
  private val authResponseEntityMapper = AuthResponseEntityMapper()
  suspend operator fun invoke(
    verifyOtpDtoModel: VerifyOtpDtoModel
  ): AuthResponseModel {
    return authResponseEntityMapper.mapFromEntity(repository.verifyOTP(verifyOtpDtoEntityMapper.mapToEntity(verifyOtpDtoModel)))
  }
}
