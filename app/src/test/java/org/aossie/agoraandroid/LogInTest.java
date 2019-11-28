package org.aossie.agoraandroid;


import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import java.io.IOException;

import retrofit2.Call;

@RunWith(MockitoJUnitRunner.class)
public class LogInTest {

    @Test
    public void LogInUnitTesting() throws IOException {


        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", "iamfake");
            jsonObject.put("password","Fakepassword");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logInResponse = apiService.logIn(jsonObject.toString());

        String Response = logInResponse.execute().message();

        Assert.assertEquals(Response,"OK");
    }

}
