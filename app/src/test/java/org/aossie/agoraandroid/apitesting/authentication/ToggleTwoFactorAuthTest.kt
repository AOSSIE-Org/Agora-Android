package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class ToggleTwoFactorAuthTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun toggleTwoFactorAuthTest() {

    val toggleTwoFactorAuthResponse: String = MockFileParser("responses/auth_responses/default_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(toggleTwoFactorAuthResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.toggleTwoFactorAuth()
      Assert.assertEquals(response.body(), toggleTwoFactorAuthResponse)
    }
  }
}
