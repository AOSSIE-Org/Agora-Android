package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.dto.LoginDto
import org.aossie.agoraandroid.data.dto.NewUserDto
import org.aossie.agoraandroid.data.dto.PasswordDto
import org.aossie.agoraandroid.data.dto.UpdateUserDto
import org.aossie.agoraandroid.data.dto.UrlDto
import org.aossie.agoraandroid.data.dto.VerifyOtpDto
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import timber.log.Timber

class UserRepository(
  private val api: Api,
  private val appDatabase: AppDatabase,
  private val preferenceProvider: PreferenceProvider
) : ApiRequest() {

  suspend fun userSignup(userData: NewUserDto): String {
    return apiRequest { api.createUser(userData) }
  }

  suspend fun userLogin(loginData: LoginDto): AuthResponse {
    return apiRequest { api.logIn(loginData) }
  }

  suspend fun verifyOTP(otpData: VerifyOtpDto): AuthResponse {
    return apiRequest { api.verifyOTP(otpData) }
  }

  suspend fun fbLogin(): AuthResponse {
    return apiRequest { api.facebookLogin() }
  }

  suspend fun getUserData(): AuthResponse {
    return apiRequest { api.getUserData() }
  }

  suspend fun saveUser(user: User) {
    appDatabase.getUserDao().removeUser()
    appDatabase.getUserDao().insert(user)
    if (user.token != null) {
      Timber.d("saved")
      preferenceProvider.setIsLoggedIn(true)
      preferenceProvider.setCurrentToken(user.token)
    }
  }

  suspend fun logout(): String {
    return apiRequest { api.logout() }
  }

  fun getUser(): LiveData<User> {
    return appDatabase.getUserDao().getUser()
  }

  suspend fun deleteUser() {
    appDatabase.getUserDao().removeUser()
    preferenceProvider.clearData()
    appDatabase.getElectionDao().deleteAllElections()
  }

  suspend fun sendForgotPasswordLink(username: String?): String {
    return apiRequest { api.sendForgotPassword(username) }
  }

  suspend fun updateUser(updateUserData: UpdateUserDto): List<String> {
    return apiRequest { api.updateUser(updateUserData) }
  }

  suspend fun changeAvatar(url: String): List<String> {
    return apiRequest { api.changeAvatar(UrlDto(url)) }
  }

  suspend fun changePassword(password: String): List<String> {
    return apiRequest { api.changePassword(PasswordDto(password)) }
  }

  suspend fun toggleTwoFactorAuth(): List<String> {
    return apiRequest { api.toggleTwoFactorAuth() }
  }

  suspend fun resendOTP(username: String?): AuthResponse {
    return apiRequest { api.resendOTP(username) }
  }
}
