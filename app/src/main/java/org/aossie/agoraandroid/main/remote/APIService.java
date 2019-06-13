package org.aossie.agoraandroid.main.remote;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {


    @FormUrlEncoded
    @POST("signup")
    Call<ResponseBody> createUser(@Field("identifier") String username,
             @Field("password") String password,
             @Field("email") String email,
             @Field("firstName") String firstName,
             @Field("lastName") String lastName);
}
