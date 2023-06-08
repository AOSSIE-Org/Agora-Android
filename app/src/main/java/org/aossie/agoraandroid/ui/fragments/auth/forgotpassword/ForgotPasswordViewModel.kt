package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.useCases.authentication.forgotPassword.SendForgotPasswordLinkUseCase
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnSendLinkClick
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnUserNameEntered
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.SnackBarActionClick
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

class ForgotPasswordViewModel
@Inject
constructor(
  private val sendForgotPasswordLinkUseCase: SendForgotPasswordLinkUseCase
) : ViewModel() {

  private val _userNameState = MutableStateFlow ("")
  val userNameState = _userNameState.asStateFlow()

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()


 private fun sendResetLink(userName: String?) = viewModelScope.launch {
   showLoading("Sending link...")
    try {
      sendForgotPasswordLinkUseCase(userName)
      _progressAndErrorState.value = progressAndErrorState.value.copy(
        isLoading = Pair("",false),
        errorResource = Pair(R.string.link_sent_please_check_your_email,true)
      )
    } catch (e: ApiException) {
      if (e.message == "412") {
        showError(AppConstants.INVALID_USERNAME_MESSAGE)
      } else {
        showError(e.message)
      }
    } catch (e: NoInternetException) {
      showError(e.message)
    } catch (e: Exception) {
      showError(e.message)
    }
  }

  fun onEvent(forgotPasswordScreenEvents: ForgotPasswordScreenEvents){
    when(forgotPasswordScreenEvents){
      OnSendLinkClick -> {
        val userName = _userNameState.value.trim()
        if (userName.isEmpty()) {
          showError("Please Enter User Name")
        } else {
          sendResetLink(userName)
        }
      }
      is OnUserNameEntered -> {
        _userNameState.value = forgotPasswordScreenEvents.userName
      }
      SnackBarActionClick -> {
        hideSnackBar()
      }
      else -> {}
    }
  }
  private fun showLoading(message: String?) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      isLoading = Pair(message!!,true)
    )
  }

  fun showError(message: String?) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      error = Pair(message!!,true),
      isLoading = Pair("",false)
    )
  }

  fun hideSnackBar() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      error = Pair("",false),
      errorResource = Pair(0,false)
    )
  }

  fun hideLoading() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      isLoading = Pair("",false)
    )
  }

}
