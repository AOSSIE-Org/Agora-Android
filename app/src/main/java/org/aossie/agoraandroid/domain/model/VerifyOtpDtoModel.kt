package org.aossie.agoraandroid.domain.model

data class VerifyOtpDtoModel(
  val crypto: String? = null,
  val otp: String? = null,
  val isTrusted: Boolean? = null,
)
