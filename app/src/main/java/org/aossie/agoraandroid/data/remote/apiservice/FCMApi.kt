package org.aossie.agoraandroid.data.remote.apiservice

import org.aossie.agoraandroid.data.remote.dto.fcm.FCMDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FCMApi {

  @POST("fcm/send")
  suspend fun sendFCM(@Body fcmDto: FCMDto): Response<Unit>
}
