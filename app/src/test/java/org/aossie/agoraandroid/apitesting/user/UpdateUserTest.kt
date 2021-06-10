package org.aossie.agoraandroid.apitesting.user

import com.squareup.moshi.Types
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.dto.UpdateUserDto
import org.aossie.agoraandroid.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.ParameterizedType

class UpdateUserTest : BaseTest<UpdateUserDto>() {
  override val type: ParameterizedType
    get() = Types.newParameterizedType(UpdateUserDto::class.java)

  @Test
  @Throws(IOException::class)
  fun updateUserTest() {

    val updateUserRequest: UpdateUserDto? = adapter?.fromJson(MockFileParser("requests/user_requests/update_user_request.json").content)
    val updateUserResponse: String = MockFileParser("responses/user_responses/update_user_response.json").content

    updateUserRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(updateUserResponse))
      runBlocking {
        GlobalScope.launch {
          val response: Response<*> = apiService.updateUser(
            it
          )
          Assert.assertEquals(response.body(), updateUserResponse)
        }
      }
    }
  }
}
