package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class LogOutTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun logOutTest() {

    val logOutResponse: String = MockFileParser("responses/user_responses/default_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(logOutResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.logout()
      Assert.assertEquals(response.body(), logOutResponse)
    }
  }
}
