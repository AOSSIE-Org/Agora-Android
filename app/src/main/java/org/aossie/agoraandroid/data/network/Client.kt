package org.aossie.agoraandroid.data.network

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.utilities.AppConstants.HTTP_INTERCEPTOR_LEVEL
import retrofit2.Retrofit

import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

private const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"
class Client
@Inject
constructor(
  private val context: Context,
  private val networkInterceptor: NetworkInterceptor
){
  private val retrofitInstance: Retrofit
    get() {
      val httpInterceptor = HttpLoggingInterceptor()
      httpInterceptor.level = HTTP_INTERCEPTOR_LEVEL

      val chuckerInterceptor = ChuckerInterceptor.Builder(context)
          .alwaysReadResponseBody(true)
          .build()

      val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(networkInterceptor)
          .addInterceptor(httpInterceptor)
          .addInterceptor(chuckerInterceptor)
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