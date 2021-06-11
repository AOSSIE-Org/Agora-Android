package org.aossie.agoraandroid.apitesting

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.aossie.agoraandroid.data.network.Api
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

open class BaseTest {
  protected val moshi = Moshi.Builder().build()

  lateinit var mockWebServer: MockWebServer
  lateinit var apiService: Api

  @Before
  @Throws(IOException::class)
  fun setup() {

    mockWebServer = MockWebServer()
    mockWebServer.start()

    apiService = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .client(OkHttpClient())
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(Api::class.java)
  }

  @After
  @Throws(IOException::class)
  fun teardown() {
    mockWebServer.shutdown()
  }
}
