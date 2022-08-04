package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.dto.VerifyOtpDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class TwoFactorAuthViewModel
@Inject
constructor(
  private val userRepository: UserRepositoryImpl
) : ViewModel() {
  lateinit var sessionExpiredListener: SessionExpiredListener

  val user = userRepository.getUser()

  private val mVerifyOtpResponse = MutableLiveData<ResponseUI<Any>>()

  val verifyOtpResponse: LiveData<ResponseUI<Any>>
    get() = mVerifyOtpResponse

  private val mResendOtpResponse = MutableLiveData<ResponseUI<Any>>()

  val resendOtpResponse: LiveData<ResponseUI<Any>>
    get() = mResendOtpResponse

  fun verifyOTP(
    otp: String,
    trustedDevice: Boolean,
    crypto: String
  ) {
    if (otp.isEmpty()) {
      mVerifyOtpResponse.value = ResponseUI.error(AppConstants.INVALID_OTP_MESSAGE)
      return
    }
    viewModelScope.launch {
      try {
        val authResponse = userRepository.verifyOTP(VerifyOtpDto(crypto, otp, trustedDevice))
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, it.trustedDevice
          )
          userRepository.saveUser(user)
          Timber.d(user.toString())
          mVerifyOtpResponse.value = ResponseUI.success()
        }
      } catch (e: ApiException) {
        mVerifyOtpResponse.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mVerifyOtpResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        mVerifyOtpResponse.value = ResponseUI.error(e.message)
      }
    }
  }

  fun resendOTP(
    username: String,
  ) {
    if (username.isEmpty()) {
      mResendOtpResponse.value = ResponseUI.error(AppConstants.LOGIN_AGAIN_MESSAGE)
      return
    }
    viewModelScope.launch {
      try {
        val authResponse = userRepository.resendOTP(username)
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, it.trustedDevice
          )
          userRepository.saveUser(user)
          mResendOtpResponse.value = ResponseUI.success()
        }
      } catch (e: ApiException) {
        mResendOtpResponse.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mResendOtpResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        mResendOtpResponse.value = ResponseUI.error(e.message)
      }
    }
  }
}
