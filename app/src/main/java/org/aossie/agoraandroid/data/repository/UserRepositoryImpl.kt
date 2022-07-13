package org.aossie.agoraandroid.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.first
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.dto.LoginDto
import org.aossie.agoraandroid.data.remote.dto.NewUserDto
import org.aossie.agoraandroid.data.remote.dto.PasswordDto
import org.aossie.agoraandroid.data.remote.dto.UpdateUserDto
import org.aossie.agoraandroid.data.remote.dto.UrlDto
import org.aossie.agoraandroid.data.remote.dto.VerifyOtpDto
import org.aossie.agoraandroid.data.remote.apiservice.Api
import org.aossie.agoraandroid.data.remote.apiservice.ApiRequest
import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.common.utilities.unsubscribeFromFCM
import timber.log.Timber

class UserRepositoryImpl(
  private val api: Api,
  private val appDatabase: AppDatabase,
  private val preferenceProvider: PreferenceProvider
) : ApiRequest() , UserRepository {


  override suspend fun userSignup(userData: NewUserDto): String {
    return apiRequest { api.createUser(userData) }
  }

  override suspend fun userLogin(loginData: LoginDto): AuthResponse {
    return apiRequest { api.logIn(loginData) }
  }

  override suspend fun refreshAccessToken(): AuthResponse {
    return apiRequest { api.refreshAccessToken() }
  }

  override suspend fun verifyOTP(otpData: VerifyOtpDto): AuthResponse {
    return apiRequest { api.verifyOTP(otpData) }
  }

  override suspend fun fbLogin(): AuthResponse {
    return apiRequest { api.facebookLogin() }
  }

  override suspend fun getUserData(): AuthResponse {
    return apiRequest { api.getUserData() }
  }

  override suspend fun saveUser(user: User) {
    appDatabase.getUserDao()
      .removeUser()
    appDatabase.getUserDao()
      .insert(user)
    if (user.authToken != null) {
      Timber.d("saved")
      preferenceProvider.setIsLoggedIn(true)
      preferenceProvider.setAccessToken(user.authToken)
      preferenceProvider.setRefreshToken(user.refreshToken)
    }
  }

  override suspend fun logout() {
    return apiRequest { api.logout() }
  }

  override fun getUser(): LiveData<User> {
    return appDatabase.getUserDao()
      .getUser()
  }

  override suspend fun deleteUser() {
    unsubscribeFromFCM(preferenceProvider.getMailId().first())
    appDatabase.getUserDao()
      .removeUser()
    preferenceProvider.clearAllData()
    appDatabase.getElectionDao()
      .deleteAllElections()
  }

  override suspend fun sendForgotPasswordLink(username: String?): String {
    return apiRequest { api.sendForgotPassword(username) }
  }

  override suspend fun updateUser(updateUserData: UpdateUserDto): List<String> {
    return apiRequest { api.updateUser(updateUserData) }
  }

  override suspend fun changeAvatar(url: String): List<String> {
    return apiRequest { api.changeAvatar(UrlDto(url)) }
  }

  override suspend fun changePassword(password: String): List<String> {
    return apiRequest { api.changePassword(PasswordDto(password)) }
  }

  override suspend fun toggleTwoFactorAuth(): List<String> {
    return apiRequest { api.toggleTwoFactorAuth() }
  }

  override suspend fun resendOTP(username: String?): AuthResponse {
    return apiRequest { api.resendOTP(username) }
  }
}
