package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BallotDto(
  @Json(name = "hash")
  val hash: String,
  @Json(name = "voteBallot")
  val voteBallot: String
)
