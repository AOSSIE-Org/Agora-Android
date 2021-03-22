package org.aossie.agoraandroid.data.network.responses

import com.google.gson.annotations.SerializedName

data class AuthResponse(
  var username: String?,
  var email: String?,
  var firstName: String?,
  var lastName: String?,
  var avatarURL: String,
  var twoFactorAuthentication: Boolean?,
  var crypto: String?,
  @SerializedName("authToken") var authToken: AuthToken?,
  var trustedDevice: String?
)