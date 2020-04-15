package org.aossie.agoraandroid.apitesting.authentication

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/** Test that checks sendForgotPassword call which changes password via asking for username */

class ForgotPasswordTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun sendForgotPasswordTest() {
    val forgotPasswordResponse: String =
      MockFileParser("responses/auth_responses/send_forgot_password_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(forgotPasswordResponse))
    val response: Response<*> = apiService.sendForgotPassword("test_name")
        .execute()
    Assert.assertEquals(response.body(), forgotPasswordResponse)
  }
}
