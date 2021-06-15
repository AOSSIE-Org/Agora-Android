package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefreshToken(
  val token: String?,
  val expiresOn: String?
)
