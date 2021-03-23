package org.aossie.agoraandroid.data.network

import okhttp3.OkHttpClient
import org.aossie.agoraandroid.data.network.interceptors.AuthorizationInterceptor
import org.aossie.agoraandroid.data.network.interceptors.NetworkInterceptor
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.data.network.responses.ElectionsResponse
import org.aossie.agoraandroid.data.network.responses.AuthToken
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

  @POST("auth/signup")
  suspend fun createUser(@Body body: String?): Response<String>

  @POST("auth/login")
  suspend fun logIn(@Body body: String): Response<AuthResponse>

  @POST("verifyOtp")
  suspend fun verifyOTP(@Body body: String): Response<AuthResponse>

  @GET("resendOtp/{userName}")
  suspend fun resendOTP(@Path("userName") userName: String?): Response<AuthResponse>

  @POST("auth/forgotPassword/send/{userName}")
  suspend fun sendForgotPassword(@Path("userName") userName: String?): Response<String>

  @GET("election")
  suspend fun getAllElections(): Response<ElectionsResponse>

  //DELETE election with specified id
  @DELETE("election/{id}")
  suspend fun deleteElection(
    @Path("id") id: String?
  ): Response<ArrayList<String>>

  //GET Ballots for election with specified id
  @GET("election/{id}/ballots")
  suspend fun getBallot(
    @Path("id") id: String?
  ): Response<Ballots>

  //GET Voters for election with specified id
  @GET("election/{id}/voters")
  suspend fun getVoters(
    @Path("id") id: String?
  ): Response<Voters>

  //POST the list of voters to election
  @POST("election/{id}/voters")
  suspend fun sendVoters(
    @Path("id") id: String?,
    @Body body: String?
  ): Response<ArrayList<String>>

  @GET("user/logout")
  suspend fun logout(): Response<String>

  //POST request to create a new election
  @POST("election")
  suspend fun createElection(
    @Body body: String?
  ): Response<ArrayList<String>>

  //update user
  @POST("user/update")
  suspend fun updateUser(
    @Body body: String?
  ): Response<ArrayList<String>>

  //change avatar
  @POST("user/changeAvatar")
  suspend fun changeAvatar(
    @Body body: String?
  ): Response<ArrayList<String>>

  //POST request to change password
  @POST("user/changePassword")
  suspend fun changePassword(
    @Body body: String?
  ): Response<ArrayList<String>>

  //GET
  @GET("toggleTwoFactorAuth")
  suspend fun toggleTwoFactorAuth(
  ): Response<ArrayList<String>>

  //GET request to log in via facebook Access Token
  //@Headers("Accept: application/json", "Content-Type: application/json")
  @GET("auth/authenticate/facebook")
  suspend fun facebookLogin(@Header("Access-Token") accessToken: String?): Response<AuthToken>

  //GET request to get user's data
  @GET("user")
  suspend fun getUserData(
  ): Response<AuthResponse>

  @GET("voter/verifyPoll/{id}")
  suspend fun verifyVoter(@Path("id") id: String?): Response<ElectionResponse>

  @POST("vote/{id}")
  suspend fun castVote(
    @Path("id") id: String?,
    @Body body: String?
  ): Response<ArrayList<String>>

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