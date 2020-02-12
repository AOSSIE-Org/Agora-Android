package org.aossie.agoraandroid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aossie.agoraandroid.remote.APIService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordTest {

    private MockWebServer mockWebServer = new MockWebServer();
    private APIService apiService;

    @Before
    public void setup() throws IOException {
        Gson gson = new GsonBuilder().setLenient().create();
        mockWebServer.start();
        apiService = new Retrofit.Builder().baseUrl(mockWebServer.url("/"))
                .client(new OkHttpClient()).addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson)).build().create(APIService.class);
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void ForgotPasswordTest() throws JSONException, IOException {

        String username = "iamfake";
        JSONObject responseBack = new JSONObject();
        responseBack.put("message","Some message");
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.sendForgotPassword(username).execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());
        MockResponse mockInvalidUsername = new MockResponse().setResponseCode(412);
        mockWebServer.enqueue(mockInvalidUsername);
        responseFromRequest = apiService.sendForgotPassword(username).execute();
        Assert.assertEquals(responseFromRequest.code(),412);

    }

}
