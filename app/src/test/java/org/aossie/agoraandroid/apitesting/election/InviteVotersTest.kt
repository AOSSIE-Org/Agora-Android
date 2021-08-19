package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.data.dto.VotersDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class InviteVotersTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun inviteVotersTest() {
    val invitationResponse: String =
      MockFileParser("responses/election_responses/default_response.json").content

    val invitationRequest: VotersDto? = VotersDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/election_requests/invite_voter_request.json").content)

    invitationRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(invitationResponse))
      testDispatcher.runBlockingTest {
        val response: Response<*> = apiService.sendVoters("id", listOf(invitationRequest))
        Assert.assertEquals(response.body(), invitationResponse)
      }
    }
  }
}
