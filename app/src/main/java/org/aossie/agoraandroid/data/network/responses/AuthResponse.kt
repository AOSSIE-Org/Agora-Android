package org.aossie.agoraandroid.data.network.responses

data class AuthResponse(
  var username: String?,
  var email: String?,
  var firstName: String?,
  var lastName: String?,
  var towFactorAuthentication: Boolean?,
  var token: Token?,
  var trustedDevice: String?
)