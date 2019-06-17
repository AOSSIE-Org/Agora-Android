package org.aossie.agoraandroid.remote;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("signup")
    Call<JSONObject> createUser(@Body JSONObject body);
}
