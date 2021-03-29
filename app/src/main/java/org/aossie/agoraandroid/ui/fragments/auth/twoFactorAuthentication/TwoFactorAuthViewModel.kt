package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import timber.log.Timber
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthViewModel.ResponseResults.SessionExpired
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class TwoFactorAuthViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
    private val prefs: PreferenceProvider
) : ViewModel() {

  val user = userRepository.getUser()

  private val mVerifyOtpResponse = MutableLiveData<ResponseResults>()

  val verifyOtpResponse: LiveData<ResponseResults>
    get() = mVerifyOtpResponse

  private val mResendOtpResponse = MutableLiveData<ResponseResults>()

  val resendOtpResponse: LiveData<ResponseResults>
    get() = mResendOtpResponse

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }
    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
    object SessionExpired : ResponseResults()
  }

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
          mVerifyOtpResponse.value = Success()
        }
      } catch (e: ApiException) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        if (e.message.toString()
                .toBoolean()
        ) verifyOTP(otp, trustedDevice, password, crypto)
        else mVerifyOtpResponse.value = SessionExpired
      } catch (e: NoInternetException) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mVerifyOtpResponse.value = Error(e.message.toString())
      }
    }
  }

  fun resendOTP(username: String, password: String) {
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
          mResendOtpResponse.value = Success()
        }
      } catch (e: ApiException) {
        mResendOtpResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        if (e.message.toString()
                .toBoolean()
        ) resendOTP(username, password)
        else mResendOtpResponse.value = SessionExpired
      } catch (e: NoInternetException) {
        mResendOtpResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mResendOtpResponse.value = Error(e.message.toString())
      }
    }
  }

}