package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.remote.dto.UpdateUserDto
import org.aossie.agoraandroid.data.remote.dto.UpdateUserDtoJsonAdapter
import org.aossie.agoraandroid.common.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class UpdateUserTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun updateUserTest() {

    val updateUserRequest: UpdateUserDto? = UpdateUserDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/user_requests/update_user_request.json").content)
    val updateUserResponse: String = MockFileParser("responses/default_response.json").content

    updateUserRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(updateUserResponse))
      testScope.launch {
        val response: Response<*> = apiService.updateUser(it)
        Assert.assertEquals(response.body(), updateUserResponse)
      }
    }
  }
}
