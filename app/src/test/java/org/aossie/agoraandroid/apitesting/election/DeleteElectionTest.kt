package org.aossie.agoraandroid.apitesting.election

import okhttp3.mockwebserver.MockResponse
import org.aossie.agoraandroid.apitesting.BaseTest
import org.aossie.agoraandroid.apitesting.Responses
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class DeleteElectionTest : BaseTest() {

    @Test
    @Throws(IOException::class)
    fun deleteElectionTest() {
        mockWebServer.enqueue(MockResponse().setBody(Responses.ELECTION_DELETE))
        val response: Response<*> = apiService.deleteElection(
                "authToken",
                "id"
        ).execute()
        Assert.assertEquals(response.body(), Responses.ELECTION_DELETE)
    }
}
