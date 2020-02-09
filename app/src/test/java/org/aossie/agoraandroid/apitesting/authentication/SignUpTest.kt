package org.aossie.agoraandroid.apitesting.authentication

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class SignUpTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun signUpTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.AUTH_SIGNUP))
    val responseFromRequest: Response<*> = apiService.createUser(Requests.AUTH_SIGNUP)
        .execute()
    Assert.assertEquals(responseFromRequest.body(), Responses.AUTH_SIGNUP)
  }
}