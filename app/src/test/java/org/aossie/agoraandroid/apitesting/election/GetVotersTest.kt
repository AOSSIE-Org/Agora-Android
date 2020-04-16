package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import okio.IOException
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response

class GetVotersTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun getVotersTest() {

    val getVotersResponse =
      MockFileParser("responses/election_responses/get_voters_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(getVotersResponse))
    val response: Response<*> = apiService.getVoters("authToken", "id")
        .execute()
    Assert.assertEquals(response.body(), getVotersResponse)

  }

}