package org.aossie.agoraandroid.data.network.dto.FCM

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NotificationDto(
  @Json(name = "title")
  val title: String? = null,
  @Json(name = "body")
  val body: String? = null,
)
