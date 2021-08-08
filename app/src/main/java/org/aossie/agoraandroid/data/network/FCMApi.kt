package org.aossie.agoraandroid.data.network

import org.aossie.agoraandroid.data.dto.FCM.FCMDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {

  @POST("fcm/send")
  suspend fun sendFCM(@Body fcmDto: FCMDto) : Response<Unit>

}
