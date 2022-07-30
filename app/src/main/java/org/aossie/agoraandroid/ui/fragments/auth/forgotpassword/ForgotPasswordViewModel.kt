package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.domain.useCases.auth_useCases.forgot_passowrd.SendForgotPasswordLinkUseCase
import javax.inject.Inject

class ForgotPasswordViewModel
@Inject
constructor(
  private val sendForgotPasswordLinkUseCase: SendForgotPasswordLinkUseCase
) : ViewModel() {

  private val _getSendResetLinkLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSendResetLinkLiveData = _getSendResetLinkLiveData
  fun sendResetLink(userName: String?) = viewModelScope.launch {
    sendForgotPasswordLinkUseCase(userName)
  }
}
