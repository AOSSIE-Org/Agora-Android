package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.data.network.dto.LoginDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class LogInTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun loginTest() {

    val loginRequest: LoginDto? = LoginDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/auth_requests/login_request.json").content)
    val loginResponse: String = MockFileParser("responses/auth_responses/auth_response.json").content

    loginRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(loginResponse))
      testScope.launch {
        val response: Response<*> = apiService.logIn(it)
        Assert.assertEquals(response.body(), loginResponse)
      }
    }
  }
}
