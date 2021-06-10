package org.aossie.agoraandroid.data.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecurityQuestionDto(
  @Json(name = "answer")
  val answer: String,
  @Json(name = "crypto")
  val crypto: String,
  @Json(name = "question")
  val question: String
)
