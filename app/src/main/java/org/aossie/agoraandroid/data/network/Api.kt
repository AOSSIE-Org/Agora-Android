package org.aossie.agoraandroid.data.network

import okhttp3.OkHttpClient
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.data.network.responses.ElectionsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"


interface Api {

  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("auth/login")
  suspend fun logIn(@Body body: String): Response<AuthResponse>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("election")
  suspend fun getAllElections(
    @Header(
        "X-Auth-Token"
    ) authToken: String?
  ): Response<ElectionsResponse>

  companion object{
    operator fun invoke(
      networkInterceptor: NetworkInterceptor
    ): Api {

      val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(networkInterceptor)
          .build()

      return Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl(BASE_URL)
          .addConverterFactory(ScalarsConverterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(Api::class.java)

    }
  }
}