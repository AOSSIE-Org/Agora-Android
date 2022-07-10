package org.aossie.agoraandroid.apitesting.authentication

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.data.remote.dto.VerifyOtpDto
import org.aossie.agoraandroid.data.remote.dto.VerifyOtpDtoJsonAdapter
import org.aossie.agoraandroid.common.utilities.MockFileParser
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class VerifyOtpTest : BaseTest() {

  @Test
  @Throws(IOException::class)
  fun verifyOtpTest() {

    val verifyOtpRequest: VerifyOtpDto? = VerifyOtpDtoJsonAdapter(moshi).fromJson(MockFileParser("requests/auth_requests/verify_otp_request.json").content)
    val verifyOtpResponse: String = MockFileParser("responses/auth_responses/auth_response.json").content

    verifyOtpRequest?.let {
      mockWebServer.enqueue(MockResponse().setBody(verifyOtpResponse))
      testScope.launch {
        val responseFromRequest: Response<*> = apiService.verifyOTP(it)
        Assert.assertEquals(responseFromRequest.body(), verifyOtpResponse)
      }
    }
  }
}
