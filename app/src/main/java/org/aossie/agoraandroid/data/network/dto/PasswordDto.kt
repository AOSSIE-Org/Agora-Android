package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PasswordDto(
  @Json(name = "password")
  val password: String? = null
)
