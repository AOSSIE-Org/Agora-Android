package org.aossie.agoraandroid.apitesting.user

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class GetUserTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun getUserTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.USER_GETUSER))
    val response: Response<*> =
      apiService.getUserData("authtoken")
          .execute()
    Assert.assertEquals(response.body(), Responses.USER_GETUSER)
  }
}