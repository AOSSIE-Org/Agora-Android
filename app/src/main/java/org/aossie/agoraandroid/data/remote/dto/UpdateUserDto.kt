package org.aossie.agoraandroid.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.remote.models.AuthToken

@JsonClass(generateAdapter = true)
data class UpdateUserDto(

  @Json(name = "username")
  val identifier: String? = null,
  @Json(name = "email")
  val email: String? = null,
  @Json(name = "firstName")
  val firstName: String? = null,
  @Json(name = "lastName")
  val lastName: String? = null,
  @Json(name = "trustedDevice")
  val trustedDevice: String? = null,
  @Json(name = "twoFactorAuthentication")
  val twoFactorAuthentication: Boolean? = null,
  @Json(name = "avatarURL")
  val avatarURL: String? = null,
  @Json(name = "authToken")
  val authToken: AuthToken? = null,
  @Json(name = "refreshToken")
  val refreshToken: AuthToken? = null,
)
