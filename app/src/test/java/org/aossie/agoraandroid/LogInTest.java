package org.aossie.agoraandroid;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.testserver.Requests;
import org.aossie.agoraandroid.testserver.Responses;
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
public class LogInTest {

    private MockWebServer mockWebServer;
    private APIService apiService;

    @Before
    public void setup() throws IOException {

        mockWebServer = new MockWebServer();
        mockWebServer.start();

        Gson gson = new GsonBuilder().setLenient().create();
        apiService = new Retrofit.Builder().baseUrl(mockWebServer.url("/"))
            .client(new OkHttpClient())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(APIService.class);
    }

    @Test
    public void LogInUnitTesting() throws IOException {

        mockWebServer.enqueue(new MockResponse().setBody(Responses.AUTH_LOGIN));

        Response response = apiService.logIn(Requests.AUTH_LOGIN).execute();

        Assert.assertEquals(response.message(), "OK");
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

}
