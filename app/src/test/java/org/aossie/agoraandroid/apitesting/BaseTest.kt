package org.aossie.agoraandroid.apitesting

import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.aossie.agoraandroid.remote.APIService
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import java.io.IOException

open class BaseTest {

  lateinit var mockWebServer: MockWebServer
  lateinit var apiService: APIService

  @Before
  @Throws(IOException::class)
  fun setup() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
    apiService = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient())
        .build()
        .create(APIService::class.java)
  }

  @After
  @Throws(IOException::class)
  fun teardown() {
    mockWebServer.shutdown()
  }

}