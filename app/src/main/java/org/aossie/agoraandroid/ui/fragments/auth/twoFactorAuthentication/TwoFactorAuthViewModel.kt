package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber

class TwoFactorAuthViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val prefs: PreferenceProvider
) : ViewModel() {

  val user = userRepository.getUser()

  private val mVerifyOtpResponse = MutableLiveData<ResponseResult>()

  val verifyOtpResponse: LiveData<ResponseResult>
    get() = mVerifyOtpResponse

  private val mResendOtpResponse = MutableLiveData<ResponseResult>()

  val resendOtpResponse: LiveData<ResponseResult>
    get() = mResendOtpResponse

  fun verifyOTP(
    otp: String,
    trustedDevice: Boolean,
    password: String,
    crypto: String
  ) {
    if (otp.isEmpty()) {
      mVerifyOtpResponse.value = Error("Invalid OTP")
      return
    }
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.verifyOTP(otp, trustedDevice, crypto)
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, password, it.trustedDevice
          )
          userRepository.saveUser(user)
          Timber.d(user.toString())
          mVerifyOtpResponse.value = Success<String>()
        }
      } catch (e: ApiException) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      }
    }
  }

  fun resendOTP(
    username: String,
    password: String
  ) {
    if (username.isEmpty()) {
      mResendOtpResponse.value = Error("Login Again")
      return
    }
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val authResponse = userRepository.resendOTP(username)
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, password, it.trustedDevice
          )
          userRepository.saveUser(user)
          mResendOtpResponse.value = Success<String>()
        }
      } catch (e: ApiException) {
        mResendOtpResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        mResendOtpResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        mResendOtpResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mResendOtpResponse.value = Error(e.message.toString())
      }
    }
  }
}
