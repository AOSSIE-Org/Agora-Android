package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.common.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class RefreshAccessTokenTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun refreshAccessTokenTest() {

    val refreshAccessTokenResponse: String = MockFileParser("responses/auth_responses/auth_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(refreshAccessTokenResponse))
    testScope.launch {
      val response: Response<*> = apiService.refreshAccessToken()
      Assert.assertEquals(response.body(), refreshAccessTokenResponse)
    }
  }
}
