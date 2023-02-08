package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginInfoDto(
  @Json(name = "providerID")
  val providerID: String? = null,
  @Json(name = "providerKey")
  val providerKey: String? = null
)
