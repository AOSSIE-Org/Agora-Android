package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.model.LoginDtoModel
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.useCases.authentication.login.LogInUseCases
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

  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData

  fun getLoggedInUser() = logInUseCases.getUser()

  fun logInRequest(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    _getLoginLiveData.value = ResponseUI.loading()
    if (identifier.isEmpty() || password.isEmpty()) {
      _getLoginLiveData.value = ResponseUI.error(AppConstants.INVALID_CREDENTIALS_MESSAGE)
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
            _getLoginLiveData.value = ResponseUI.success()
          } else {
            _getLoginLiveData.value = ResponseUI.success(user.crypto!!)
          }
        }
      } catch (e: ApiException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
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
    _getLoginLiveData.value = ResponseUI.loading()
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
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
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
        _getLoginLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      }
    }
  }
}
