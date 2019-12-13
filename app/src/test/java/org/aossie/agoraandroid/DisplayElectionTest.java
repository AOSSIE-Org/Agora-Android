package org.aossie.agoraandroid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aossie.agoraandroid.remote.APIService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DisplayElectionTest {

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
    public void getBallotTest() throws JSONException, IOException {

        JSONArray responseBack = new JSONArray();
        JSONObject responseObject = new JSONObject();
        responseObject.put("voteBallot","string");
        responseObject.put("hashes","string");
        responseBack.put(responseObject);
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.getBallot("tokentext","randomidstring").execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());

    }

    @Test
    public void getVotersTest() throws JSONException, IOException {

        JSONArray responseBack = new JSONArray();
        JSONObject responseObject = new JSONObject();
        responseObject.put("name","string");
        responseObject.put("hashes","string");
        responseBack.put(responseObject);
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.getVoters("tokentext","randomidstring").execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());

    }

    @Test
    public void deleteElectionTest() throws JSONException, IOException {

        JSONObject responseBack = new JSONObject();
        responseBack.put("message","string");
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.deleteElection("tokentext","randomidstring").execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());

    }






}
