package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VerifyOtpDto(
  val crypto: String,
  val otp: String,
  @Json(name = "trustedDevice")
  val isTrusted: Boolean,

)
