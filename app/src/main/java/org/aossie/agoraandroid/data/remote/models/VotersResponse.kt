package org.aossie.agoraandroid.data.remote.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.remote.dto.VotersDto

@JsonClass(generateAdapter = true)
data class VotersResponse(
  @Json(name = "voters")
  val voterList: List<VotersDto>
)
