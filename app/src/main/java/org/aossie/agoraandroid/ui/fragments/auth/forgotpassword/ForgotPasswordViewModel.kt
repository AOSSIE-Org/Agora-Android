package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.repository.UserRepositoryImpl
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import javax.inject.Inject

class ForgotPasswordViewModel
@Inject
constructor(
  private val userRepository: UserRepositoryImpl
) : ViewModel() {

  private val _getSendResetLinkLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSendResetLinkLiveData = _getSendResetLinkLiveData
  fun sendResetLink(userName: String?) = viewModelScope.launch {
    _getSendResetLinkLiveData.value = ResponseUI.loading()
    try {
      userRepository.sendForgotPasswordLink(userName)
      _getSendResetLinkLiveData.value = ResponseUI.success()
    } catch (e: ApiException) {
      if (e.message == "412") {
        _getSendResetLinkLiveData.value = ResponseUI.error(AppConstants.INVALID_USERNAME_MESSAGE)
      } else {
        getSendResetLinkLiveData.value = ResponseUI.error(e.message)
      }
    } catch (e: NoInternetException) {
      getSendResetLinkLiveData.value = ResponseUI.error(e.message)
    } catch (e: Exception) {
      getSendResetLinkLiveData.value = ResponseUI.error(e.message)
    }
  }
}
