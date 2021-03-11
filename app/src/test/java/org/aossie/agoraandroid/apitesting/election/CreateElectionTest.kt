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

class CreateElectionTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun createElectionTest() {

    val createElectionRequest:String=MockFileParser("requests/election_requests/create_election_request.json").content
    val createElectionResponse:String=MockFileParser("responses/election_responses/create_election_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(createElectionResponse))
    runBlocking {
      GlobalScope.launch {
        val responseFromRequest: Response<*> =
          apiService.createElection(createElectionRequest, "authtoken")
        Assert.assertEquals(responseFromRequest.body(), createElectionResponse)
      }
    }
  }
}