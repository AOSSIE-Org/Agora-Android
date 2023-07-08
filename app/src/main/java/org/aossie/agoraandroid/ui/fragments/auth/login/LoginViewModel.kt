package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.LoginDtoModel
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.useCases.authentication.login.LogInUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginUiEvent
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginViewModelEvent
import org.aossie.agoraandroid.ui.screens.auth.models.LoginModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
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

  private val _loginDataState = MutableStateFlow (LoginModel())
  val loginDataState = _loginDataState.asStateFlow()

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  private val _uiEvents = MutableSharedFlow<LoginUiEvent>()
  val uiEvents = _uiEvents.asSharedFlow()

  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginStateFlow: MutableStateFlow<ResponseUI<String>?> =
    MutableStateFlow(null)
  val getLoginStateFlow: StateFlow<ResponseUI<String>?> = _getLoginStateFlow

  fun getLoggedInUser() = logInUseCases.getUser()

  fun logInRequest(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    showLoading("Authenticating...")
    _getLoginStateFlow.value = ResponseUI.loading()
    if (identifier.isEmpty() || password.isEmpty()) {
      hideLoading()
      showError(AppConstants.INVALID_CREDENTIALS_MESSAGE)
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
          hideSnackBar()
          if (!it.twoFactorAuthentication!!) {
            _uiEvents.emit(LoginUiEvent.UserLoggedIn)
            _getLoginStateFlow.value = ResponseUI.success()
          } else {
            _uiEvents.emit(LoginUiEvent.OnTwoFactorAuthentication(user.crypto!!))
            _getLoginStateFlow.value = ResponseUI.success(user.crypto!!)
          }
        }
      } catch (e: ApiException) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        showError(e.message)
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
    showLoading("Facebook Login...")
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
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        showError(e.message)
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
        hideLoading()
        hideSnackBar()
        _uiEvents.emit(LoginUiEvent.UserLoggedIn)
        _getLoginStateFlow.value = ResponseUI.success()
      } catch (e: ApiException) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        showError(e.message)
        _getLoginStateFlow.value = ResponseUI.error(e.message)
      }
    }
  }

  fun onEvent(event: LoginViewModelEvent) {
    when(event){
      is LoginViewModelEvent.EnteredPassword -> {
        _loginDataState.value = loginDataState.value.copy(
          password = event.password
        )
      }
      is LoginViewModelEvent.EnteredUserName -> {
        _loginDataState.value = loginDataState.value.copy(
          username = event.username
        )
      }
      LoginViewModelEvent.LoginClick -> {
        if(_loginDataState.value.username.isEmpty()){
          _progressAndErrorState.value = progressAndErrorState.value.copy(
            errorResource = Pair(R.string.invalid_username,true)
          )
          return
        }
        if(_loginDataState.value.password.isEmpty()){
          _progressAndErrorState.value = progressAndErrorState.value.copy(
            errorResource = Pair(R.string.invalid_password,true)
          )
          return
        }
        logInRequest(_loginDataState.value.username, _loginDataState.value.password)
      }
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
