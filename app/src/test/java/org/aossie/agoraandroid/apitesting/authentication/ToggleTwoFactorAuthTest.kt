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
class ToggleTwoFactorAuthTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun toggleTwoFactorAuthTest() {

    val toggleTwoFactorAuthResponse: String = MockFileParser("responses/default_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(toggleTwoFactorAuthResponse))
    testScope.launch {
      val response: Response<*> = apiService.toggleTwoFactorAuth()
      Assert.assertEquals(response.body(), toggleTwoFactorAuthResponse)
    }
  }
}
