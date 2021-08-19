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

/** Test that checks sendForgotPassword call which changes password via asking for username */

@ExperimentalCoroutinesApi
class ForgotPasswordTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun sendForgotPasswordTest() {

    val forgotPasswordResponse: String = MockFileParser("responses/auth_responses/default_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(forgotPasswordResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.sendForgotPassword("test_name")
      Assert.assertEquals(response.body(), forgotPasswordResponse)
    }
  }
}
