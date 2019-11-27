package org.aossie.agoraandroid;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import retrofit2.Call;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class LogInUnitTest {

    private static final String LOGIN_GRANTED = "OK";
    @Test
    public void LogInUnitTesting() throws IOException {


        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", "fakeusername");
            jsonObject.put("password","fakepassword");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logInResponse = apiService.logIn(jsonObject.toString());

        String Response = logInResponse.execute().message();

        assertThat(Response,is(LOGIN_GRANTED));
    }

}
