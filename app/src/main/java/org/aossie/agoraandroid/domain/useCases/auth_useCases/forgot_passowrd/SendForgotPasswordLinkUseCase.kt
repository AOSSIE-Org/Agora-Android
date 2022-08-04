package org.aossie.agoraandroid.domain.useCases.auth_useCases.forgot_passowrd

import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class SendForgotPasswordLinkUseCase @Inject constructor(
  private val repository: UserRepository
) {
  private val _getSendResetLinkLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSendResetLinkLiveData = _getSendResetLinkLiveData
  suspend operator fun invoke(
    username : String?
  ) {
    _getSendResetLinkLiveData.value = ResponseUI.loading()
    try {
      repository.sendForgotPasswordLink(username)
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