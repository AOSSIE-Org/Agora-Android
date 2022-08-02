package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VerifyOtpDto(
  val crypto: String? = null,
  val otp: String? = null,
  @Json(name = "trustedDevice")
  val isTrusted: Boolean? = null,

)
