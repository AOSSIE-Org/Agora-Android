package org.aossie.agoraandroid.apitesting.authentication

import com.squareup.moshi.Types
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.LoginDto
import org.aossie.agoraandroid.data.dto.PasswordDto
import org.aossie.agoraandroid.utilities.MockFileParser
import org.apache.tools.ant.taskdefs.Execute.launch
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

class LogInTest : BaseTest<LoginDto>() {

  override val type: ParameterizedType?
    get() = Types.newParameterizedType(PasswordDto::class.java)

  @Test
  @Throws(IOException::class)
  fun loginTest() {

    val loginRequest: LoginDto? = adapter?.fromJson(MockFileParser("requests/auth_requests/login_request.json").content)
    val loginResponse: String = MockFileParser("responses/auth_responses/login_response.json").content

    loginRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(loginResponse))
      runBlocking {
        GlobalScope.launch {
          val response: Response<*> = apiService.logIn(it)
          Assert.assertEquals(response.body(), loginResponse)
        }
      }
    }
  }
}
