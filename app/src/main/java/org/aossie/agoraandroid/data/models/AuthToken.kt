package org.aossie.agoraandroid.data.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthToken(
  val token: String?,
  val expiresOn: String?
)
