package org.aossie.agoraandroid.apitesting

import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.aossie.agoraandroid.data.network.api.Api
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

@ExperimentalCoroutinesApi
open class BaseTest {
  protected val moshi = Moshi.Builder().build()

  lateinit var mockWebServer: MockWebServer
  lateinit var apiService: Api

  private val testDispatcher = TestCoroutineDispatcher()
  val testScope = TestCoroutineScope(testDispatcher)

  @Before
  @Throws(IOException::class)
  fun setup() {

    mockWebServer = MockWebServer()
    mockWebServer.start()

    apiService = Retrofit.Builder()
      .baseUrl(mockWebServer.url("/"))
      .client(OkHttpClient())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(Api::class.java)

    Dispatchers.setMain(testDispatcher)
  }

  @After
  @Throws(IOException::class)
  fun teardown() {
    mockWebServer.shutdown()
    testDispatcher.cleanupTestCoroutines()
  }
}
