package org.aossie.agoraandroid.apitesting.user

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class ChangePasswordTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun changePasswordTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.USER_CHANGE_PASSWORD))
    val response: Response<*> =
      apiService.changePassword(Requests.USER_CHANGE_PASSWORD, "authtoken")
          .execute()
    Assert.assertEquals(response.body(), Responses.USER_CHANGE_PASSWORD)
  }
}