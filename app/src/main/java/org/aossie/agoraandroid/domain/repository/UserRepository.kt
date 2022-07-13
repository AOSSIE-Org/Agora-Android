package org.aossie.agoraandroid.domain.repository

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.data.remote.dto.LoginDto
import org.aossie.agoraandroid.data.remote.dto.NewUserDto
import org.aossie.agoraandroid.data.remote.dto.UpdateUserDto
import org.aossie.agoraandroid.data.remote.dto.VerifyOtpDto

interface UserRepository {
  suspend fun userSignup(userData: NewUserDto): String
  suspend fun userLogin(loginData: LoginDto): AuthResponse
  suspend fun refreshAccessToken(): AuthResponse
  suspend fun verifyOTP(otpData: VerifyOtpDto): AuthResponse
  suspend fun fbLogin(): AuthResponse
  suspend fun getUserData(): AuthResponse
  suspend fun saveUser(user: User)
  suspend fun logout()
  fun getUser(): LiveData<User>
  suspend fun deleteUser()
  suspend fun sendForgotPasswordLink(username: String?): String
  suspend fun updateUser(updateUserData: UpdateUserDto): List<String>
  suspend fun changeAvatar(url: String): List<String>
  suspend fun changePassword(password: String): List<String>
  suspend fun toggleTwoFactorAuth(): List<String>
  suspend fun resendOTP(username: String?): AuthResponse
}