package org.aossie.agoraandroid.data.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BallotDto(
  @Json(name = "hash")
  val hash: String? = null,
  @Json(name = "voteBallot")
  val voteBallot: String? = null
)
