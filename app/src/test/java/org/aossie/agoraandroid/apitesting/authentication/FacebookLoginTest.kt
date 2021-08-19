package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class FacebookLoginTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun facebookLoginUnitTest() {

    val facebookLoginResponse: String = MockFileParser("responses/auth_responses/auth_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(facebookLoginResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.facebookLogin()
      Assert.assertEquals(response.body(), facebookLoginResponse)
    }
  }
}
