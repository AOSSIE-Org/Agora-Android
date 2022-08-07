package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.network.responses.AuthToken

class UpdateUserDtoModel(
  val identifier: String? = null,
  val email: String? = null,
  val firstName: String? = null,
  val lastName: String? = null,
  val trustedDevice: String? = null,
  val twoFactorAuthentication: Boolean? = null,
  val avatarURL: String? = null,
  val authToken: AuthToken? = null,
  val refreshToken: AuthToken? = null,
)
