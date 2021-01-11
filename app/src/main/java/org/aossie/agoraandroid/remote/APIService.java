package org.aossie.agoraandroid.remote;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

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

  //POST request to create a new election
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("election")
  Call<String> createElection(@Body String body, @Header("X-Auth-Token") String authToken);

  //POST Request ToSend Link Of Forgot Password
  @POST("auth/forgotPassword/send/{userName}")
  Call<String> sendForgotPassword(@Path("userName") String userName);

  //GET Request To Perform Logout
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("user/logout")
  Call<String> logout(@Header("X-Auth-Token") String authToken);

  //GET Request To Get Data of All Elections
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("election")
  Call<String> getAllElections(@Header("X-Auth-Token") String authToken);

  //GET request to log in via facebook Access Token
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("auth/authenticate/facebook")
  Call<String> facebookLogin(@Header("Access-Token") String accessToken);

  //GET request to get user's data
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("user")
  Call<String> getUserData(@Header("X-Auth-Token") String authToken);

  //DELETE election with specified id
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @DELETE("election/{id}")
  Call<String> deleteElection(@Header("X-Auth-Token") String authToken, @Path("id") String id);

  //GET Ballots for election with specified id
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("election/{id}/ballots")
  Call<String> getBallot(@Header("X-Auth-Token") String authToken, @Path("id") String id);

  //GET Voters for election with specified id
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("election/{id}/voters")
  Call<String> getVoters(@Header("X-Auth-Token") String authToken, @Path("id") String id);

  //POST the list of voters to election
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("election/{id}/voters")
  Call<String> sendVoters(@Header("X-Auth-Token") String authToken, @Path("id") String id,
      @Body String body);

  //GET Result for election with specified id
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @GET("result/{id}")
  Call<String> getResult(@Header("X-Auth-Token") String authToken, @Path("id") String id);

  //update user
  @Headers({
      "Accept: application/json",
      "Content-Type: application/json"
  })
  @POST("user/update")
  Call<String> updateUser(@Header("X-Auth-Token") String authToken, @Body String body);
}
