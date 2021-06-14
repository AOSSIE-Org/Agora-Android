package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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
    runBlocking {
      GlobalScope.launch {
        val response: Response<*> = apiService.getVoters("id")
        Assert.assertEquals(response.body(), getVotersResponse)
      }
    }
  }
}
