package org.aossie.agoraandroid.apitesting.authentication

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test

import retrofit2.Response
import java.io.IOException

class LogInTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun LogInUnitTesting() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.AUTH_LOGIN))
    val response: Response<*> = apiService.logIn(Requests.AUTH_LOGIN)
        .execute()
    Assert.assertEquals(response.body(), Responses.AUTH_LOGIN)
  }

}