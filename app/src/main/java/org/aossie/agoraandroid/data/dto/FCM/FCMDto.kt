package org.aossie.agoraandroid.data.dto.FCM

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class FCMDto(
  @Json(name = "to")
  val to: String? = null,
  @Json(name = "notification")
  val notification: NotificationDto? = null,
  @Json(name = "data")
  val data: DataDto? = null
)
