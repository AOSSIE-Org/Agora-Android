package org.aossie.agoraandroid.apitesting.authentication

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class ForgotPasswordTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun ForgotPasswordUnitTesting() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.AUTH_FORGOT_PASSWORD))
    val response: Response<*> = apiService.sendForgotPassword(Requests.AUTH_FORGOT_PASSWORD)
        .execute()
    Assert.assertEquals(response.body(), Responses.AUTH_FORGOT_PASSWORD)
  }
}
