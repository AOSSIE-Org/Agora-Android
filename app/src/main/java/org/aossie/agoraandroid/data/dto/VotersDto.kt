package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotersDto(
  @Json(name = "name")
  val voterName: String,
  @Json(name = "hash")
  val voterEmail: String,
)
