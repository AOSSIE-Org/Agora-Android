package org.aossie.agoraandroid.data.repository

import org.aossie.agoraandroid.data.remote.dto.FCM.DataDto
import org.aossie.agoraandroid.data.remote.dto.FCM.FCMDto
import org.aossie.agoraandroid.data.remote.dto.FCM.NotificationDto
import org.aossie.agoraandroid.data.remote.apiservice.ApiRequest
import org.aossie.agoraandroid.data.remote.apiservice.FCMApi
import org.aossie.agoraandroid.domain.repository.FCMRepository
import javax.inject.Inject

class FCMRepositoryImpl
@Inject
constructor(
  private val fcmApi: FCMApi,
) : ApiRequest(), FCMRepository {

  override suspend fun sendFCM(
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
