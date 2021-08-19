package org.aossie.agoraandroid.apitesting.user

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

/** Tests getUser call that retrieves user data from backend */

@ExperimentalCoroutinesApi
class GetUserTest : BaseTest() {
  @Test
  @Throws(IOException::class)
  fun getUserTest() {

    val getUserResponse: String = MockFileParser("responses/user_responses/get_user_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(getUserResponse))
    testDispatcher.runBlockingTest {
      val response: Response<*> = apiService.getUserData()
      Assert.assertEquals(response.body(), getUserResponse)
    }
    }
}
