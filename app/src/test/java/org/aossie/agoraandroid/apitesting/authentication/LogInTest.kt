package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.MockFileParser
import org.apache.tools.ant.taskdefs.Execute.launch
import org.junit.Assert
import org.junit.Test

import retrofit2.Response
import java.io.IOException

class LogInTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun loginTest() {

    val loginRequest:String=MockFileParser("requests/auth_requests/login_request.json").content
    val loginResponse:String=MockFileParser("responses/auth_responses/login_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(loginResponse))
    runBlocking{
      GlobalScope.launch {
        val response: Response<*> = apiService.logIn(loginRequest)
        Assert.assertEquals(response.body(), loginResponse)
      }
    }
  }

}