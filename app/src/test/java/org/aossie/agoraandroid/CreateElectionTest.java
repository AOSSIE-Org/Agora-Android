package org.aossie.agoraandroid;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

@RunWith(MockitoJUnitRunner.class)
public class CreateElectionTest {

    @Test
    public void CreateElectionUnitTest() throws IOException, JSONException {
        ArrayList<String> candidates = new ArrayList<>();
        candidates.add("faker");
        candidates.add("fakers");
        JSONArray jsArray = new JSONArray(candidates);
        String token = LoginForToken();
        final JSONObject jsonObject = new JSONObject();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("voteBallot", "");
            map.put("hash", "");
            JSONArray ballot = new JSONArray(new Map[]{map});
            jsonObject.put("ballot", ballot);  //Append the other JSONObject to the parent one
            jsonObject.put("name", "New Election Name");
            jsonObject.put("description", "New Election Decription");
            jsonObject.put("voterListVisibility", true);
            jsonObject.put("startingDate","2019-11-30T11:57:28Z" );
            jsonObject.put("endingDate", "2019-12-01T11:57:28Z");
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
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> electionResponse = apiService.createElection(jsonObject.toString(), token);
        String Response = electionResponse.execute().message();
        Assert.assertEquals(Response,"OK");

    }

    private String LoginForToken() throws IOException, JSONException {

        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", "iamfake");
            jsonObject.put("password","Fakepassword");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logInResponse = apiService.logIn(jsonObject.toString());

        JSONObject jsonObjects = new JSONObject(logInResponse.execute().body());

        JSONObject token = jsonObjects.getJSONObject("token");
        String key = token.getString("token");

        return key;
    }


}
