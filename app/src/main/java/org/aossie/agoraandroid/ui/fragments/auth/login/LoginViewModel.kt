package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.FaceBookLogInUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.GetUserDataUseCase_
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.RefreshAccessTokenUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.UserLogInUseCase
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
  private val userLoginUseCase: UserLogInUseCase,
  private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
  private val faceBookLogInRequestUseCase: FaceBookLogInUseCase,
  private val getUserDataUseCase_: GetUserDataUseCase_,
  private val getUserUseCase: GetUserUseCase,
) : ViewModel() {

  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData

  fun getLoggedInUser() = getUserUseCase()

  fun logInRequest(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    viewModelScope.launch {
     userLoginUseCase(identifier,password,trustedDevice)
    }
  }

  fun refreshAccessToken(
    trustedDevice: String? = null
  ) {
    viewModelScope.launch {
      refreshAccessTokenUseCase(trustedDevice)
    }
  }

  fun facebookLogInRequest() {
    _getLoginLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      faceBookLogInRequestUseCase()
    }
  }

  private fun getUserData(authResponse: AuthResponse) {
    viewModelScope.launch {
      getUserDataUseCase_(authResponse)
    }
  }
}
