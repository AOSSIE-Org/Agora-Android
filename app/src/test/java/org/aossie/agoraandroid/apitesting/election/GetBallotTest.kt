package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response


/** Test that checks getBallot Call that helps in retrieving ballot data */

class GetBallotTest() : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun getBallotTest() {

    val getBallotResponse = MockFileParser("responses/election_responses/ballot_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(getBallotResponse))
    val responseFromRequest: Response<*> = apiService.getBallot("authToken", "id")
        .execute()
    Assert.assertEquals(responseFromRequest.body(), getBallotResponse)

  }

}