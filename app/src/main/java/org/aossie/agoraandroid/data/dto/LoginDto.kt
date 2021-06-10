package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginDto(
  @Json(name = "identifier")
  val identifier: String,
  @Json(name = "trustedDevice")
  val trustedDevice: String,
  @Json(name = "password")
  val password: String

)
