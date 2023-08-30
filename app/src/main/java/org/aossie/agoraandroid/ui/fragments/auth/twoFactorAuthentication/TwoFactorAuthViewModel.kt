package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.model.VerifyOtpDtoModel
import org.aossie.agoraandroid.domain.useCases.authentication.twoFactorAuthentication.TwoFactorAuthUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class TwoFactorAuthViewModel
@Inject
constructor(
  private val twoFactorAuthUseCases: TwoFactorAuthUseCases
) : ViewModel() {
  lateinit var sessionExpiredListener: SessionExpiredListener

  private var user:UserModel? = null

  private val _uiEventsFlow = MutableSharedFlow<UiEvents>()
  val uiEventsFlow = _uiEventsFlow.asSharedFlow()

  private val _progressAndErrorState = mutableStateOf(ScreensState())
  val progressAndErrorState: State<ScreensState> = _progressAndErrorState

  init {
    viewModelScope.launch {
      twoFactorAuthUseCases.getUser().collectLatest {
        user = it
      }
    }
  }

  fun verifyOTP(
    otp: String,
    trustedDevice: Boolean
  ) {
    if (otp.isEmpty()) {
      showMessage(string.enter_otp)
      return
    }
    if(!trustedDevice) {
      showMessage(R.string.tap_on_checkbox)
      return
    }
    if(user == null) {
      showMessage(R.string.something_went_wrong_please_try_again_later)
      return
    }
    viewModelScope.launch {
      showLoading(R.string.verifying_otp)
      try {
        val authResponse =
          twoFactorAuthUseCases.verifyOTP(VerifyOtpDtoModel(user!!.crypto, otp, trustedDevice))
        authResponse.let {
          val user = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, it.trustedDevice
          )
          twoFactorAuthUseCases.saveUser(user)
          Timber.d(user.toString())
          hideLoading()
          _uiEventsFlow.emit(UiEvents.TwoFactorAuthComplete)
        }
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  fun resendOTP() {
    if(user == null) {
      showMessage(R.string.something_went_wrong_please_try_again_later)
      return
    }
    val username = user!!.username
    if (username!!.isEmpty()) {
      showMessage(AppConstants.LOGIN_AGAIN_MESSAGE)
      return
    }
    viewModelScope.launch {
      showLoading(R.string.sending_otp)
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
          showMessage(R.string.otp_sent)
        }
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
      }
    }
  }

  private fun showLoading(message: Any) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  private fun showMessage(message: Any) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      message = Pair("",false),
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }

  sealed class UiEvents{
    object TwoFactorAuthComplete:UiEvents()
  }
}