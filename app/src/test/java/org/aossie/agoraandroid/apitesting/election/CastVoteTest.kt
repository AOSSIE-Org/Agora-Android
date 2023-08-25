package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.network.dto.CastVoteDto
import org.aossie.agoraandroid.data.network.dto.CastVoteDtoJsonAdapter
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class CastVoteTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun castVoteTest() {

    val castVoteRequest: CastVoteDto? = CastVoteDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/election_requests/cast_vote_request.json").content)
    val castVoteResponse: String = MockFileParser("responses/default_response.json").content

    castVoteRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(castVoteResponse))
      testScope.launch {
        val response: Response<*> = apiService.castVote("id", it)
        Assert.assertEquals(response.body(), castVoteResponse)
      }
    }
  }
}
