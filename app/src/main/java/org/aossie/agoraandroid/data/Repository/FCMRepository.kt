package org.aossie.agoraandroid.data.Repository

import org.aossie.agoraandroid.data.network.api.ApiRequest
import org.aossie.agoraandroid.data.network.api.FCMApi
import org.aossie.agoraandroid.data.network.dto.FCM.DataDto
import org.aossie.agoraandroid.data.network.dto.FCM.FCMDto
import org.aossie.agoraandroid.data.network.dto.FCM.NotificationDto
import javax.inject.Inject

class FCMRepository
@Inject
constructor(
  private val fcmApi: FCMApi,
) : ApiRequest() {

  suspend fun sendFCM(
    topicId: String,
    title: String,
    body: String,
    electionId: String,
  ) {
    return apiRequest {
      fcmApi.sendFCM(
        FCMDto(
          "/topics/$topicId",
          NotificationDto(title, body),
          DataDto(electionId)
        )
      )
    }
  }
}
