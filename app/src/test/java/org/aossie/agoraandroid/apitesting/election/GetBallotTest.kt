package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

/** Test that checks getBallot Call that helps in retrieving ballot data */

@ExperimentalCoroutinesApi
class GetBallotTest() : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun getBallotTest() {

    val getBallotResponse = MockFileParser("responses/election_responses/ballot_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(getBallotResponse))
    testScope.launch {
      val responseFromRequest: Response<*> = apiService.getBallot("authToken")
      Assert.assertEquals(responseFromRequest.body(), getBallotResponse)
    }
  }
}
