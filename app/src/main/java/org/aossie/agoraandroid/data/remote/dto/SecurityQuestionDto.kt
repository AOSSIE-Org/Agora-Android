package org.aossie.agoraandroid.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SecurityQuestionDto(
  @Json(name = "answer")
  val answer: String? = null,
  @Json(name = "crypto")
  val crypto: String? = null,
  @Json(name = "question")
  val question: String? = null
)
