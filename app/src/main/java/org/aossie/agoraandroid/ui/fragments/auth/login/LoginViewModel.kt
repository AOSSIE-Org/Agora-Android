package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.domain.use_cases.authentication.login.FaceBookLogInUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.GetUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.RefreshAccessTokenUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.SaveUserUseCase
import org.aossie.agoraandroid.domain.use_cases.authentication.login.UserLogInUseCase
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.aossie.agoraandroid.utilities.subscribeToFCM
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
  private val prefs: PreferenceProvider,
  private val logInUseCases: LogInUseCases
) : ViewModel() {

  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginStateFlow: MutableStateFlow<ResponseUI<String>?> =
    MutableStateFlow(null)
  val getLoginStateFlow: StateFlow<ResponseUI<String>?> = _getLoginStateFlow.asStateFlow()

  fun getLoggedInUser() = logInUseCases.getUser()

  fun logInRequest(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    _getLoginStateFlow.value = ResponseUI.loading()
    if (identifier.isEmpty() || password.isEmpty()) {
      _getLoginStateFlow.value = ResponseUI.error(AppConstants.INVALID_CREDENTIALS_MESSAGE)
      return
    }
    viewModelScope.launch {
      try {
        val authResponse =
          logInUseCases.userLogIn(LoginDtoModel(identifier, trustedDevice, password))
        authResponse.let {
          val user = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, trustedDevice
          )
          logInUseCases.saveUser(user)
          it.email?.let { mail ->
            prefs.setMailId(mail)
            subscribeToFCM(mail)
          }
          Timber.d(user.toString())
          if (!it.twoFactorAuthentication!!) {
            _getLoginStateFlow.value = ResponseUI.success()
          } else {
            _getLoginStateFlow.value = ResponseUI.success(user.crypto!!)
          }
        }
      } catch (e: ApiException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      }
    }
  }

  fun refreshAccessToken(
    trustedDevice: String? = null
  ) {
    viewModelScope.launch {
      try {
        val authResponse = logInUseCases.refreshAccessToken()
        authResponse.let {
          val user = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, trustedDevice
          )
          logInUseCases.saveUser(user)
        }
      } catch (e: Exception) {
        sessionExpiredListener?.onSessionExpired()
      }
    }
  }

  fun facebookLogInRequest() {
    _getLoginStateFlow.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val authResponse = logInUseCases.faceBookLogIn()
        getUserData(authResponse)
        authResponse.email?.let {
          prefs.setMailId(it)
          subscribeToFCM(it)
        }
        Timber.d(authResponse.toString())
      } catch (e: ApiException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      }
    }
  }

  private fun getUserData(authResponse: AuthResponseModel) {
    viewModelScope.launch {
      try {
        val user = UserModel(
          authResponse.username, authResponse.email, authResponse.firstName, authResponse.lastName,
          authResponse.avatarURL, authResponse.crypto, authResponse.twoFactorAuthentication,
          authResponse.authToken?.token, authResponse.authToken?.expiresOn,
          authResponse.refreshToken?.token, authResponse.refreshToken?.expiresOn,
          authResponse.trustedDevice
        )
        logInUseCases.saveUser(user)
        Timber.d(authResponse.toString())
        prefs.setIsFacebookUser(true)
        _getLoginStateFlow.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      }
    }
  }
}
