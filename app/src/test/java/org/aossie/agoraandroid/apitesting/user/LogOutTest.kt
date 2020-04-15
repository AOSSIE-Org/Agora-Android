package org.aossie.agoraandroid.apitesting.user

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class LogOutTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun logOutTest() {

    val logOutResponse:String=MockFileParser("responses/user_responses/logout_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(logOutResponse))
    val response: Response<*> =
      apiService.logout("authtoken")
          .execute()
    Assert.assertEquals(response.body(), logOutResponse)
  }
}