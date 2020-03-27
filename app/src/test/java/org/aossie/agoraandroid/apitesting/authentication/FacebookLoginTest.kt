package org.aossie.agoraandroid.apitesting.authentication

import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class FacebookLoginTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun facebookLoginUnitTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.AUTH_FACEBOOK_LOGIN))
    val response: Response<*> = apiService.facebookLogin(Requests.AUTH_FACEBOOK_LOGIN)
        .execute()
    Assert.assertEquals(response.body(), Responses.AUTH_FACEBOOK_LOGIN)
  }
}