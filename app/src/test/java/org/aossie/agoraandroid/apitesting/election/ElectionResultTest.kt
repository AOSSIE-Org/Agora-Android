package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/** Test that checks getResult Call that helps in retrieving election results*/

class ElectionResultTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun electionResultTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.ELECTION_RESULT))
    val responseFromRequest: Response<*> = apiService.getResult("authToken", "id")
        .execute()
    Assert.assertEquals(responseFromRequest.body(), Responses.ELECTION_RESULT)
  }

}