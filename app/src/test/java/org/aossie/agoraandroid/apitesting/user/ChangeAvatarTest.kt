package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.UrlDto
import org.aossie.agoraandroid.data.dto.UrlDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class ChangeAvatarTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun changeAvatarTest() {

    val changeAvatarRequest: UrlDto? = UrlDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/user_requests/change_avatar_request.json").content)
    val changeAvatarResponse: String = MockFileParser("responses/default_response.json").content

    changeAvatarRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(changeAvatarResponse))
      testScope.launch {
        val response: Response<*> = apiService.changeAvatar(it)
        Assert.assertEquals(response.body(), changeAvatarResponse)
      }
    }
  }
}
