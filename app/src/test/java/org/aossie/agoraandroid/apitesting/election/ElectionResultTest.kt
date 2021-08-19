package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/** Test that checks getResult Call that helps in retrieving election results*/

@ExperimentalCoroutinesApi
class ElectionResultTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun electionResultTest() {

    val electionResultResponse: String = MockFileParser("responses/election_responses/election_result_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(electionResultResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.getResult("id")
      Assert.assertEquals(response.body(), electionResultResponse)
    }
  }
}
