package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.PasswordDto
import org.aossie.agoraandroid.data.dto.PasswordDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/** Test that checks changePassword call which changes password via asking contemporary password and authToken */

@ExperimentalCoroutinesApi
class ChangePasswordTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun changePasswordTest() {

    val changePasswordRequest: PasswordDto? = PasswordDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/auth_requests/change_password_request.json").content)
    val changePasswordResponse = MockFileParser("responses/auth_responses/default_response.json").content

    changePasswordRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(changePasswordResponse))
      testDispatcher.runBlockingTest {
        val response: Response<*> = apiService.changePassword(it)
        Assert.assertEquals(response.body(), changePasswordResponse)
      }
    }
  }
}
