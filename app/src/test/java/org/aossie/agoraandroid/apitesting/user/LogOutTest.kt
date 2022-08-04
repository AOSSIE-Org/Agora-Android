package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.common.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class LogOutTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun logOutTest() {

    val logOutResponse: String = MockFileParser("responses/default_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(logOutResponse))
    testScope.launch {
      val response: Response<*> = apiService.logout()
      Assert.assertEquals(response.body(), logOutResponse)
    }
  }
}
