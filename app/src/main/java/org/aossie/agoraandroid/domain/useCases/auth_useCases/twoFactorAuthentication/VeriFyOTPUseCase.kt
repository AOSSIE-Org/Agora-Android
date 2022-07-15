package org.aossie.agoraandroid.domain.useCases.auth_useCases.twoFactorAuthentication

import org.aossie.agoraandroid.data.remote.dto.VerifyOtpDto
import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class VeriFyOTPUseCase @Inject constructor(
  private val repository: UserRepository
) {
  suspend operator fun invoke(
    verifyOtpDto: VerifyOtpDto
  ) : AuthResponse{
    return repository.verifyOTP(verifyOtpDto)
  }
}