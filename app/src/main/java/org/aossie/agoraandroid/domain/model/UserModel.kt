package org.aossie.agoraandroid.domain.model

import org.aossie.agoraandroid.data.db.entities.CURRENT_USER_ID

data class UserModel(
  val username: String? = null,
  val email: String? = null,
  var firstName: String? = null,
  var lastName: String? = null,
  val avatarURL: String? = null,
  val crypto: String? = null,
  val twoFactorAuthentication: Boolean? = null,
  var authToken: String? = null,
  var authTokenExpiresOn: String? = null,
  var refreshToken: String? = null,
  var refreshTokenExpiresOn: String? = null,
  val trustedDevice: String? = null
) {
  var uid: Int = CURRENT_USER_ID
}
