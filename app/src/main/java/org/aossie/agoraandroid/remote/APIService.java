package org.aossie.agoraandroid.remote;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

@SuppressWarnings("SpellCheckingInspection")
public interface APIService {

    //POST Request For SignUp
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @POST("auth/signup")
    Call<String> createUser(@Body String body);

    //POST request for LogIn
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("auth/login")
    Call<String> logIn(@Body String body);



    //POST request to change password
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })

    @POST("user/changePassword")
    Call<String> changePassword(@Body String body, @Header("X-Auth-Token") String authToken);




}
