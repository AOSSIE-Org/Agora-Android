package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.NewUserDto
import org.aossie.agoraandroid.data.dto.NewUserDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class SignUpTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun signUpTest() {

    val signUpRequest: NewUserDto? = NewUserDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/auth_requests/signUp_request.json").content)
    val signUpResponse: String = MockFileParser("responses/auth_responses/signUp_response.json").content

    signUpRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(signUpResponse))
      testDispatcher.runBlockingTest {
        val responseFromRequest: Response<*> = apiService.createUser(it)
        Assert.assertEquals(responseFromRequest.body(), signUpResponse)
      }
    }
  }
}
