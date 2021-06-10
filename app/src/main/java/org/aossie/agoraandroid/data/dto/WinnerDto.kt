package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WinnerDto(
  @Json(name = "candidate")
  val candidate: CandidateDto,
  @Json(name = "score")
  val score: ScoreDto
)
