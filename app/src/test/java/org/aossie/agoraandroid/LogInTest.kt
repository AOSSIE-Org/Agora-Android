package org.aossie.agoraandroid

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.aossie.agoraandroid.remote.APIService
import org.aossie.agoraandroid.testserver.Requests
import org.aossie.agoraandroid.testserver.Responses
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

@RunWith(MockitoJUnitRunner::class)
class LogInTest {

  private lateinit var mockWebServer: MockWebServer
  private lateinit var apiService: APIService

  @Before
  @Throws(IOException::class)
  fun setup() {
    mockWebServer = MockWebServer()
    mockWebServer.start()
    val gson = GsonBuilder().setLenient().create()
    apiService = Retrofit.Builder().baseUrl(mockWebServer.url("/"))
        .client(OkHttpClient())
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(APIService::class.java)
  }

  @Test
  @Throws(IOException::class)
  fun LogInUnitTesting() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.AUTH_LOGIN))
    val response: Response<*> = apiService.logIn(Requests.AUTH_LOGIN).execute()
    Assert.assertEquals(response.message(), "OK")
  }

  @After
  @Throws(IOException::class)
  fun teardown() {
    mockWebServer.shutdown()
  }
}