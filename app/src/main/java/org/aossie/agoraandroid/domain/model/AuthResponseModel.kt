package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.network.responses.AuthToken

data class AuthResponseModel(
  val username: String?,
  val email: String?,
  val firstName: String?,
  val lastName: String?,
  val avatarURL: String?,
  val twoFactorAuthentication: Boolean?,
  val crypto: String?,
  val authToken: AuthToken?,
  val refreshToken: AuthToken?,
  val trustedDevice: String?
)