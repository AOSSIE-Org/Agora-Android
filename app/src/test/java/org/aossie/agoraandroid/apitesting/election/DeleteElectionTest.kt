package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class DeleteElectionTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun deleteElectionTest() {

    val deleteElectionResponse:String=MockFileParser("responses/election_responses/delete_election_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(deleteElectionResponse))
    val response: Response<*> = apiService.deleteElection(
        "authToken",
        "id"
      )
      .execute()
    Assert.assertEquals(response.body(), deleteElectionResponse)
  }
}