package org.aossie.agoraandroid.data.network.api

import org.aossie.agoraandroid.data.network.dto.FCM.FCMDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {

  @POST("fcm/send")
  suspend fun sendFCM(@Body fcmDto: FCMDto): Response<Unit>
}
