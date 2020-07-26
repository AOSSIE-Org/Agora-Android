package org.aossie.agoraandroid.data.network.responses

data class AuthResponse(
  var username: String?,
  var email: String?,
  var firstName: String?,
  var lastName: String?,
  var avatarURL: String,
  var twoFactorAuthentication: Boolean?,
  var crypto: String?,
  var token: Token?,
  var trustedDevice: String?
)