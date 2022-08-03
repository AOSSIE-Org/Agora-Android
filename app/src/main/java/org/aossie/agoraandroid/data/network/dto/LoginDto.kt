package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginDto(
  @Json(name = "identifier")
  val identifier: String? = null,
  @Json(name = "trustedDevice")
  val trustedDevice: String? = null,
  @Json(name = "password")
  val password: String? = null

)
