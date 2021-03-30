package org.aossie.agoraandroid.data.network.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
  val username: String?,
  val email: String?,
  val firstName: String?,
  val lastName: String?,
  val avatarURL: String,
  val twoFactorAuthentication: Boolean?,
  val crypto: String?,
  @SerializedName("authToken") val authToken: AuthToken?,
  val trustedDevice: String?
)
