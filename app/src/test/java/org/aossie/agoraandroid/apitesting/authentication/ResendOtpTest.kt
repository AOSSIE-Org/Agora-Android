package org.aossie.agoraandroid.apitesting.authentication

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
class ResendOtpTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun resendOtpTest() {

    val resendOtpResponse: String = MockFileParser("responses/auth_responses/auth_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(resendOtpResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.resendOTP("user_name")
      Assert.assertEquals(response.body(), resendOtpResponse)
    }
  }
}
