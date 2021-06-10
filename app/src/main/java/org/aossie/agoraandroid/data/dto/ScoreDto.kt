package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ScoreDto(
  @Json(name = "denominator")
  val denominator: Int,
  @Json(name = "numerator")
  val numerator: Int
)
