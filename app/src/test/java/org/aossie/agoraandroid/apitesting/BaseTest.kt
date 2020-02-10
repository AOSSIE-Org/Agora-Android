package org.aossie.agoraandroid.apitesting

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.aossie.agoraandroid.remote.APIService
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

open class BaseTest {

  lateinit var mockWebServer: MockWebServer
  lateinit var apiService: APIService

  @Before
  @Throws(IOException::class)
  fun setup() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
    val gson = GsonBuilder().setLenient()
        .create()
    apiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(APIService::class.java)
  }

  @After
  @Throws(IOException::class)
  fun teardown() {
    mockWebServer.shutdown()
  }

}