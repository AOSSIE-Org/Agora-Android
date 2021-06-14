package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class FacebookLoginTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun facebookLoginUnitTest() {

    val facebookLoginResponse: String = MockFileParser("responses/auth_responses/facebook_login_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(facebookLoginResponse))
    runBlocking {
      GlobalScope.launch {
        val response: Response<*> = apiService.facebookLogin()
        Assert.assertEquals(response.body(), facebookLoginResponse)
      }
    }
  }
}
