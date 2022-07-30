package org.aossie.agoraandroid.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CastVoteDto(
  @Json(name = "ballotInput")
  val ballotInput: String? = null,
  @Json(name = "passCode")
  val passCode: String? = null,
)
