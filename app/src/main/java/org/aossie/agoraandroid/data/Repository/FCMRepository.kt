package org.aossie.agoraandroid.data.Repository

import org.aossie.agoraandroid.data.dto.FCM.DataDto
import org.aossie.agoraandroid.data.dto.FCM.FCMDto
import org.aossie.agoraandroid.data.dto.FCM.NotificationDto
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.FCMApi
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
