package org.aossie.agoraandroid.apitesting.user

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class UpdateUserTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun updateUserTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.UPDATE_USER))
    val response: Response<*> = apiService.updateUser(
            "authToken",
            Requests.UPDATE_USER
        )
        .execute()
    Assert.assertEquals(response.body(), Responses.UPDATE_USER)
  }
}