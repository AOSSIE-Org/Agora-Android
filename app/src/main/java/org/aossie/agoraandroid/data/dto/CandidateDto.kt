package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CandidateDto(
  @Json(name = "id")
  val id: String? = null,
  @Json(name = "name")
  val name: String? = null,
  @Json(name = "party")
  val party: String? = null
)
