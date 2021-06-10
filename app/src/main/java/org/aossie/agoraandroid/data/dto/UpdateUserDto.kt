package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.network.responses.AuthToken

@JsonClass(generateAdapter = true)
data class UpdateUserDto(

  @Json(name = "username")
  val identifier: String,
  val email: String,
  val firstName: String? = null,
  val lastName: String? = null,
  val trustedDevice: String? = null,
  val twoFactorAuthentication: Boolean? = null,
  val avatarURL: String? = null,
  val authToken: AuthToken,

)
