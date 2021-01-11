package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class InviteVotersTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun inviteVotersTest() {
    val invitationResponse: String =
      MockFileParser("responses/election_responses/invite_voter_response.json").content

    val invitationRequest: String =
      MockFileParser("requests/election_requests/invite_voter_request.json").content

    mockWebServer.enqueue(MockResponse().setBody(invitationResponse))
    runBlocking {
      GlobalScope.launch {
        val response: Response<*> = apiService.sendVoters(
            "authToken",
            "id",
            invitationRequest
        )
        Assert.assertEquals(response.body(), invitationResponse)
      }
    }
  }
}


