package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class SignUpTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun signUpTest() {

    val signUpRequest:String=MockFileParser("requests/auth_requests/signUp_request.json").content
    val signUpResponse:String=MockFileParser("responses/auth_responses/signUp_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(signUpResponse))
    runBlocking{
      GlobalScope.launch {
        val responseFromRequest: Response<*> = apiService.createUser(signUpRequest)
        Assert.assertEquals(responseFromRequest.body(), signUpResponse)
      }
    }
  }
}