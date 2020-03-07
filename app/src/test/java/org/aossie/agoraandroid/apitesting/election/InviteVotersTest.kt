package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class InviteVotersTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun inviteVotersTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.INVITE_VOTERS))
    val response: Response<*> = apiService.sendVoters(
        "authToken",
        "id",
        Requests.INVITE_VOTERS
    )
        .execute()
    Assert.assertEquals(response.body(), Responses.INVITE_VOTERS)
  }
}