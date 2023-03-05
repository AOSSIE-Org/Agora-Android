package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthToken(
  val token: String?,
  val expiresOn: String?
)
