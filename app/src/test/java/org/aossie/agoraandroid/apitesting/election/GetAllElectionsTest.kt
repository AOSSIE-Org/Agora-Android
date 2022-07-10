package org.aossie.agoraandroid.apitesting.election

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.common.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class GetAllElectionsTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun getAllElectionsTest() {

    val getAllElectionsResponse: String = MockFileParser("responses/election_responses/get_all_elections_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(getAllElectionsResponse))
    testScope.launch {
      val response: Response<*> = apiService.getAllElections()
      Assert.assertEquals(response.body(), getAllElectionsResponse)
    }
  }
}
