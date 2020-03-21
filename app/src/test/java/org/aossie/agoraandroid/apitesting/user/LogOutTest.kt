package org.aossie.agoraandroid.apitesting.user

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class LogOutTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun logOutTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.USER_LOGOUT))
    val response: Response<*> =
      apiService.logout("authtoken")
          .execute()
    Assert.assertEquals(response.body(), Responses.USER_LOGOUT)
  }
}