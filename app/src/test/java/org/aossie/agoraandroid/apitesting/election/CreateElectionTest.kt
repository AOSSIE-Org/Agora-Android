package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Requests
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class CreateElectionTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun createElectionTest() {
    mockWebServer.enqueue(MockResponse().setBody(Responses.ELECTION_CREATE))
    val responseFromRequest: Response<*> =
      apiService.createElection(Requests.ELECTION_CREATE, "authtoken")
          .execute()
    Assert.assertEquals(responseFromRequest.body(), Responses.ELECTION_CREATE)
  }
}