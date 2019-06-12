package org.aossie.agoraandroid.main.Remote;

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
             @Field("firstname") String firstname,
             @Field("lastname") String lastname);
}
