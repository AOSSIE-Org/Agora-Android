package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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

class ChangePasswordTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun changePasswordTest() {

    val changePasswordResponse = MockFileParser("responses/auth_responses/change_password_response.json").content
    val changePasswordRequest = PasswordDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/auth_requests/change_password_request.json").content) ?: PasswordDto("")

    mockWebServer.enqueue(MockResponse().setBody(changePasswordResponse))
    runBlocking {
      GlobalScope.launch {
        val response: Response<*> =
          apiService.changePassword(changePasswordRequest)
        Assert.assertEquals(response.body(), changePasswordResponse)
      }
    }
  }
}
