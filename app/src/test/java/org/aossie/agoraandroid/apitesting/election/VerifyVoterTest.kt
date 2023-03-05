package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class VerifyVoterTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun verifyVoterTest() {

    val verifyVoterResponse: String = MockFileParser("responses/election_responses/verify_voter_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(verifyVoterResponse))
    testScope.launch {
      val response: Response<*> = apiService.verifyVoter("id")
      Assert.assertEquals(response.body(), verifyVoterResponse)
    }
  }
}
