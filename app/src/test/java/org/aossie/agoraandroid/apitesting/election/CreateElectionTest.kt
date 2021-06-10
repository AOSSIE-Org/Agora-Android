package org.aossie.agoraandroid.apitesting.election

import com.squareup.moshi.Types
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

class CreateElectionTest : BaseTest<ElectionDto>() {

  override val type: ParameterizedType
    get() = Types.newParameterizedType(ElectionDto::class.java)
  @Test
  @Throws(IOException::class)
  fun createElectionTest() {

    val createElectionRequest: ElectionDto? = adapter?.fromJson(MockFileParser("requests/election_requests/create_election_request.json").content)
    val createElectionDto: String = MockFileParser("responses/election_responses/create_election_response.json").content

    createElectionRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(createElectionDto))
      runBlocking {
        GlobalScope.launch {
          val responseFromRequest: Response<*> =
            apiService.createElection(it)
          Assert.assertEquals(responseFromRequest.body(), createElectionDto)
        }
      }
    }
  }
}
