package org.aossie.agoraandroid.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

private const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"
class Client
@Inject
constructor(
  private val networkInterceptor: NetworkInterceptor
){
  private val retrofitInstance: Retrofit
    get() {
      val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(networkInterceptor)
          .build()
      return Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl(BASE_URL)
          .addConverterFactory(ScalarsConverterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()
    }

  val api : Api
    get() = retrofitInstance
        .create(
            Api::class.java
        )
}