package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthResponse(
  val username: String?,
  val email: String?,
  val firstName: String?,
  val lastName: String?,
  val avatarURL: String?,
  val twoFactorAuthentication: Boolean?,
  val crypto: String?,
  @Json(name = "authToken") val authToken: AuthToken?,
  @Json(name = "refreshToken") val refreshToken: AuthToken?,
  val trustedDevice: String?
)
