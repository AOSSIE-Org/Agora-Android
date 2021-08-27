package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.dto.VotersDto

@JsonClass(generateAdapter = true)
data class VotersResponse(
  @Json(name = "voters")
  val voterList: List<VotersDto>
)
