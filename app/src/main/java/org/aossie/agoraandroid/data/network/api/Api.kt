package org.aossie.agoraandroid.data.network.api

import org.aossie.agoraandroid.data.network.dto.CastVoteDto
import org.aossie.agoraandroid.data.network.dto.ElectionDto
import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.data.network.dto.NewUserDto
import org.aossie.agoraandroid.data.network.dto.PasswordDto
import org.aossie.agoraandroid.data.network.dto.UpdateUserDto
import org.aossie.agoraandroid.data.network.dto.UrlDto
import org.aossie.agoraandroid.data.network.dto.VerifyOtpDto
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionListResponse
import org.aossie.agoraandroid.data.network.responses.VotersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface Api {

  @POST("auth/signup")
  suspend fun createUser(@Body userdto: NewUserDto): Response<String>

  @POST("auth/login")
  suspend fun logIn(@Body loginDto: LoginDto): Response<AuthResponse>

  @GET("auth/refreshAccessToken")
  suspend fun refreshAccessToken(): Response<AuthResponse>

  @POST("verifyOtp")
  suspend fun verifyOTP(@Body verifyOtpDto: VerifyOtpDto): Response<AuthResponse>

  @GET("resendOtp/{userName}")
  suspend fun resendOTP(@Path("userName") userName: String?): Response<AuthResponse>

  @POST("auth/forgotPassword/send/{userName}")
  suspend fun sendForgotPassword(@Path("userName") userName: String?): Response<String>

  @GET("election")
  suspend fun getAllElections(): Response<ElectionListResponse>

  // DELETE election with specified id
  @DELETE("election/{id}")
  suspend fun deleteElection(@Path("id") id: String?): Response<List<String>>

  // GET Ballots for election with specified id
  @GET("election/{id}/ballots")
  suspend fun getBallot(@Path("id") id: String?): Response<Ballots>

  // GET Voters for election with specified id
  @GET("election/{id}/voters")
  suspend fun getVoters(@Path("id") id: String?): Response<VotersResponse>

  // POST the list of voters to election
  @POST("election/{id}/voters")
  suspend fun sendVoters(
    @Path("id") id: String?,
    @Body votersDto: List<VotersDto>
  ): Response<List<String>>

  @GET("user/logout")
  suspend fun logout(): Response<Unit>

  // POST request to create a new election
  @POST("election")
  suspend fun createElection(@Body electionDto: ElectionDto): Response<List<String>>

  // update user
  @POST("user/update")
  suspend fun updateUser(@Body updateUserDto: UpdateUserDto): Response<List<String>>

  // change avatar
  @POST("user/changeAvatar")
  suspend fun changeAvatar(@Body url: UrlDto): Response<List<String>>

  // POST request to change password
  @POST("user/changePassword")
  suspend fun changePassword(@Body password: PasswordDto): Response<List<String>>

  // GET
  @GET("toggleTwoFactorAuth")
  suspend fun toggleTwoFactorAuth(): Response<List<String>>

  // GET request to log in via facebook Access Token
  @GET("auth/authenticate/facebook")
  suspend fun facebookLogin(): Response<AuthResponse>

  // GET request to get user's data
  @GET("user")
  suspend fun getUserData(): Response<AuthResponse>

  @GET("voter/verifyPoll/{id}")
  suspend fun verifyVoter(@Path("id") id: String?): Response<ElectionDto>

  @POST("vote/{id}")
  suspend fun castVote(
    @Path("id") id: String?,
    @Body body: CastVoteDto
  ): Response<List<String>>

  @GET("result/{id}")
  suspend fun getResult(@Path("id") id: String?): Response<List<WinnerDto>>
}
