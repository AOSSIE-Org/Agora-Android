package org.aossie.agoraandroid.apitesting.authentication

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

    val facebookLoginResponse:String= MockFileParser("responses/auth_responses/facebookLoginResponse.json").content

    mockWebServer.enqueue(MockResponse().setBody(facebookLoginResponse))
    val response: Response<*> = apiService.facebookLogin("authToken")
        .execute()
    Assert.assertEquals(response.body(), facebookLoginResponse)
  }
}