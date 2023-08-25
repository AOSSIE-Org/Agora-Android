package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionListResponse(
  @Json(name = "elections")
  val elections: List<ElectionResponse>
)
