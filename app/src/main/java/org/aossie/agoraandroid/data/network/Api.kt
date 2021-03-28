package org.aossie.agoraandroid.data.network

import okhttp3.OkHttpClient
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.data.network.responses.ElectionsResponse
import org.aossie.agoraandroid.data.network.responses.Voters
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL = "https://agora-rest-api.herokuapp.com/api/v1/"


interface Api {

  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("auth/signup")
  suspend fun createUser(@Body body: String?): Response<String>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("auth/login")
  suspend fun logIn(@Body body: String): Response<AuthResponse>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("verifyOtp")
  suspend fun verifyOTP(@Body body: String): Response<AuthResponse>

  @GET("resendOtp/{userName}")
  suspend fun resendOTP(@Path("userName") userName: String?): Response<AuthResponse>

  @POST("auth/forgotPassword/send/{userName}")
  suspend fun sendForgotPassword(@Path("userName") userName: String?): Response<String>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("election")
  suspend fun getAllElections(
    @Header(
        "X-Auth-Token"
    ) authToken: String?
  ): Response<ElectionsResponse>

  //DELETE election with specified id
  @Headers("Accept: application/json", "Content-Type: application/json")
  @DELETE("election/{id}")
  suspend fun deleteElection(@Header("X-Auth-Token") authToken: String?, @Path("id") id: String?): Response<ArrayList<String>>

  //GET Ballots for election with specified id
  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("election/{id}/ballots")
  suspend fun getBallot(@Header("X-Auth-Token") authToken: String?, @Path("id") id: String?): Response<Ballots>

  //GET Voters for election with specified id
  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("election/{id}/voters")
  suspend fun getVoters(@Header("X-Auth-Token") authToken: String?, @Path("id") id: String?): Response<Voters>

  //POST the list of voters to election
  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("election/{id}/voters")
  suspend fun sendVoters(@Header("X-Auth-Token") authToken: String?, @Path("id") id: String?, @Body body: String?): Response<ArrayList<String>>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("user/logout")
  suspend fun logout(
    @Header(
        "X-Auth-Token"
    ) authToken: String?): Response<String>

  //POST request to create a new election
  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("election")
  suspend fun createElection(@Body body: String?, @Header("X-Auth-Token") authToken: String?): Response<ArrayList<String>>

  //update user
  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("user/update")
  suspend fun updateUser(@Header("X-Auth-Token") authToken: String?, @Body body: String?): Response<ArrayList<String>>

  //change avatar
  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("user/changeAvatar")
  suspend fun changeAvatar(@Header("X-Auth-Token") authToken: String?, @Body body: String?): Response<ArrayList<String>>

  //POST request to change password
  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("user/changePassword")
  suspend fun changePassword(@Body body: String?, @Header("X-Auth-Token") authToken: String?): Response<ArrayList<String>>

  //GET
  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("toggleTwoFactorAuth")
  suspend fun toggleTwoFactorAuth(@Header("X-Auth-Token") authToken: String?): Response<ArrayList<String>>

  //GET request to log in via facebook Access Token
  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("auth/authenticate/facebook")
  suspend fun facebookLogin(@Header("Access-Token") accessToken: String?): Response<AuthResponse>

  //GET request to get user's data
  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("user")
  suspend fun getUserData(@Header("X-Auth-Token") authToken: String?): Response<AuthResponse>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @GET("voter/verifyPoll/{id}")
  suspend fun verifyVoter(@Path("id") id: String?): Response<ElectionResponse>

  @Headers("Accept: application/json", "Content-Type: application/json")
  @POST("vote/{id}")
  suspend fun castVote(@Path("id") id: String?, @Body body: String?): Response<ArrayList<String>>

  companion object{
    operator fun invoke(
      networkInterceptor: NetworkInterceptor,
      authorizationInterceptor: AuthorizationInterceptor
    ): Api {

      val okHttpClient = OkHttpClient.Builder()
          .addInterceptor(networkInterceptor)
          .addInterceptor(authorizationInterceptor)
          .build()

      return Retrofit.Builder()
          .client(okHttpClient)
          .baseUrl(BASE_URL)
          .addConverterFactory(ScalarsConverterFactory.create())
          .addConverterFactory(GsonConverterFactory.create())
          .build()
          .create(Api::class.java)

    }
  }
}