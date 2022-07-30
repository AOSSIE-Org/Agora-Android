package org.aossie.agoraandroid.data.remote.dto.fcm

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationDto(
  @Json(name = "title")
  val title: String? = null,
  @Json(name = "body")
  val body: String? = null,
)
