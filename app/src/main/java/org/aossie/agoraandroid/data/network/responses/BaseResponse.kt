package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BaseResponse(
  @Json(name = "message")
  val message: String
)
