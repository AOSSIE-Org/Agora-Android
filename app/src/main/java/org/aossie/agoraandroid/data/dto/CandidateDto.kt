package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CandidateDto(
  @Json(name = "id")
  val id: String,
  @Json(name = "name")
  val name: String,
  @Json(name = "party")
  val party: String
)
