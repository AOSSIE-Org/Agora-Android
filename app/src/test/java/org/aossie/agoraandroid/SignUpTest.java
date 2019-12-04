package org.aossie.agoraandroid;

import android.os.Build;

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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Config(sdk = Build.VERSION_CODES.O_MR1)
@RunWith(RobolectricTestRunner.class)
public class SignUpTest {
    private MockWebServer mockWebServer = new MockWebServer();
    private APIService apiService;

    @Before
    public void setup() throws IOException {
        Gson gson = new GsonBuilder().setLenient().create();
        mockWebServer.start();
        apiService = new Retrofit.Builder().baseUrl(mockWebServer.url("/")).client(new OkHttpClient()).addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create(gson)).build().create(APIService.class);
    }

    @After
    public void teardown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void signUpTest() throws JSONException, IOException {
        //Body for post request
        JSONObject jsonObject = new JSONObject();
        JSONObject securityJsonObject = new JSONObject();
        jsonObject.put("identifier", "fakePersonName");
        jsonObject.put("password", "fakePassword");
        jsonObject.put("email", "fakeEmail@gmail.com");
        jsonObject.put("firstName", "fakeFirstName");
        jsonObject.put("lastName", "fakeLastName");
        securityJsonObject.put("question", "fakeQuestion");
        securityJsonObject.put("answer", "fakeAnswer");
        jsonObject.put("securityQuestion",securityJsonObject);
        //Response Body
        JSONObject responseBack = new JSONObject();
        responseBack.put("token","Some token string");
        responseBack.put("expiresOn","Some date time or timestamp");
        MockResponse mockResponse = new MockResponse().setResponseCode(200).setBody(responseBack.toString());
        mockWebServer.enqueue(mockResponse);
        Response responseFromRequest = apiService.createUser(jsonObject.toString()).execute();
        Assert.assertEquals(responseFromRequest.body(),responseBack.toString());
        MockResponse mockUserNameExists = new MockResponse().setResponseCode(409);
        mockWebServer.enqueue(mockUserNameExists);
        responseFromRequest = apiService.createUser(jsonObject.toString()).execute();
        Assert.assertEquals(responseFromRequest.code(),409);
    }
}