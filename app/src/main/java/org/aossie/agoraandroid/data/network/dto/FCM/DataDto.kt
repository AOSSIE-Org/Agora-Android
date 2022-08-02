package org.aossie.agoraandroid.data.dto.FCM

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.aossie.agoraandroid.utilities.AppConstants

@JsonClass(generateAdapter = true)
data class DataDto(
  @Json(name = AppConstants.ELECTION_ID)
  val electionId: String? = null
)
