package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UrlDto(
  @Json(name = "url")
  val url: String? = null
)
