package org.aossie.agoraandroid;

import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@RunWith(MockitoJUnitRunner.class)
public class CreateElectionTest {
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
    public void createElectionTest() throws JSONException, IOException {
        //Body for post request
        final JSONObject jsonObject = new JSONObject();
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("faker");
        candidates.add("fakers");
        JSONArray jsArray = new JSONArray(candidates);
        try {
            Map<String, String> map = new HashMap<>();
            map.put("voteBallot", "");
            map.put("hash", "");
            JSONArray ballot = new JSONArray(new Map[]{map});
            jsonObject.put("ballot", ballot);  //Append the other JSONObject to the parent one
            jsonObject.put("name", "New Election Name");
            jsonObject.put("description", "New Election Decription");
            jsonObject.put("voterListVisibility", true);
            jsonObject.put("startingDate","somedatestamp" );
            jsonObject.put("endingDate", "some date stamp");
            jsonObject.put("isInvite", true);
            jsonObject.put("ballotVisibility", "Ballots are completely secret and never shown to anyone");
            jsonObject.put("isRealTime", true);
            jsonObject.put("votingAlgo", "Oklahoma");
            jsonObject.put("candidates", jsArray);
            jsonObject.put("noVacancies", 1);
            jsonObject.put("electionType","Election");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Response Body
        JSONArray responseBack = new JSONArray();
        responseBack.put("message");
        responseBack.put("Election created successfuly");
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.createElection(jsonObject.toString(),"authtokenss").execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());

    }
}
