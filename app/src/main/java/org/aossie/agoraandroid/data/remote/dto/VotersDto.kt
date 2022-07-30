package org.aossie.agoraandroid.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VotersDto(
  @Json(name = "name")
  val voterName: String? = null,
  @Json(name = "hash")
  val voterEmail: String? = null,
)
