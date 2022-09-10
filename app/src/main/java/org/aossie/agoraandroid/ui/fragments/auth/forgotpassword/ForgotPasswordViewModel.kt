package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.useCases.authentication.forgotPassword.SendForgotPasswordLinkUseCase
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import javax.inject.Inject

class ForgotPasswordViewModel
@Inject
constructor(
  private val sendForgotPasswordLinkUseCase: SendForgotPasswordLinkUseCase
) : ViewModel() {

  private val _getSendResetLinkStateFlow: MutableStateFlow<ResponseUI<Any>?> =
    MutableStateFlow(null)
  val getSendResetLinkStateFlow: StateFlow<ResponseUI<Any>?> =
    _getSendResetLinkStateFlow

  fun sendResetLink(userName: String?) = viewModelScope.launch {
    _getSendResetLinkStateFlow.value = ResponseUI.loading()
    try {
      sendForgotPasswordLinkUseCase(userName)
      _getSendResetLinkStateFlow.value = ResponseUI.success()
    } catch (e: ApiException) {
      if (e.message == "412") {
        _getSendResetLinkStateFlow.value = ResponseUI.error(AppConstants.INVALID_USERNAME_MESSAGE)
      } else {
        _getSendResetLinkStateFlow.value = ResponseUI.error(e.message)
      }
    } catch (e: NoInternetException) {
      _getSendResetLinkStateFlow.value = ResponseUI.error(e.message)
    } catch (e: Exception) {
      _getSendResetLinkStateFlow.value = ResponseUI.error(e.message)
    }
  }
}
