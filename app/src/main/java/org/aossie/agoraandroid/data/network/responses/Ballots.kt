package org.aossie.agoraandroid.data.network.responses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.data.dto.BallotDto

@JsonClass(generateAdapter = true)
data class Ballots(
  @Json(name = "ballots")
  val ballots: List<BallotDto>
)
