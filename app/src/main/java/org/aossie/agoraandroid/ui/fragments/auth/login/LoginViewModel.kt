package org.aossie.agoraandroid.ui.fragments.auth.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.dto.LoginDto
import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val prefs: PreferenceProvider
) : ViewModel() {

  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData

  fun getLoggedInUser() = userRepository.getUser()

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
          userRepository.userLogin(LoginDto(identifier, trustedDevice ?: "", password))
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, trustedDevice
          )
          userRepository.saveUser(user)
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
        val authResponse = userRepository.refreshAccessToken()
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, trustedDevice
          )
          userRepository.saveUser(user)
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
        val authResponse = userRepository.fbLogin()
        getUserData(authResponse)
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

  private fun getUserData(authResponse: AuthResponse) {
    viewModelScope.launch {
      try {
        val user = User(
          authResponse.username, authResponse.email, authResponse.firstName, authResponse.lastName,
          authResponse.avatarURL, authResponse.crypto, authResponse.twoFactorAuthentication,
          authResponse.authToken?.token, authResponse.authToken?.expiresOn,
          authResponse.refreshToken?.token, authResponse.refreshToken?.expiresOn,
          authResponse.trustedDevice
        )
        userRepository.saveUser(user)
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
