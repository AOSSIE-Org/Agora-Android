package org.aossie.agoraandroid.apitesting.user

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

class LogOutTest : BaseTest<Any>() {
  @Test
  @Throws(IOException::class)
  fun logOutTest() {

    val logOutResponse: String = MockFileParser("responses/user_responses/logout_response.json").content

    mockWebServer.enqueue(MockResponse().setBody(logOutResponse))
    runBlocking {
      GlobalScope.launch {
        val response: Response<*> =
          apiService.logout()
        Assert.assertEquals(response.body(), logOutResponse)
      }
    }
  }
}
