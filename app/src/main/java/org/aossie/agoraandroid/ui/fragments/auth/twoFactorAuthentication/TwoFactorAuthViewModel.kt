package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.model.VerifyOtpDtoModel
import org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication.TwoFactorAuthUseCases
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
  private val twoFactorAuthUseCases: TwoFactorAuthUseCases
) : ViewModel() {
  lateinit var sessionExpiredListener: SessionExpiredListener

  val user = twoFactorAuthUseCases.getUser()

  private val mVerifyOtpResponse = MutableStateFlow<ResponseUI<Any>?>(null)

  val verifyOtpResponse: StateFlow<ResponseUI<Any>?>
    get() = mVerifyOtpResponse.asStateFlow()

  private val mResendOtpResponse = MutableStateFlow<ResponseUI<Any>?>(null)

  val resendOtpResponse: StateFlow<ResponseUI<Any>?>
    get() = mResendOtpResponse.asStateFlow()

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
        val authResponse = twoFactorAuthUseCases.verifyOTP(VerifyOtpDtoModel(crypto, otp, trustedDevice))
        authResponse.let {
          val user = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, it.trustedDevice
          )
          twoFactorAuthUseCases.saveUser(user)
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
        val authResponse = twoFactorAuthUseCases.resendOTP(username)
        authResponse.let {
          val user = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, it.trustedDevice
          )
          twoFactorAuthUseCases.saveUser(user)
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
