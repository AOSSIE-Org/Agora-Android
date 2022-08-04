package org.aossie.agoraandroid.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.dto.UpdateUserDto
import org.aossie.agoraandroid.data.remote.models.AuthToken
import org.aossie.agoraandroid.data.repository.UserRepositoryImpl
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.GetUserUseCase
import org.aossie.agoraandroid.domain.useCases.auth_useCases.login.SaveUserUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ChangeAvatarUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ChangePasswordUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.GetUserDataUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.ToggleTwoFactorAuthenticationUseCase
import org.aossie.agoraandroid.domain.useCases.updateprofile.UpdateUserUseCase
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val getUserUseCase: GetUserUseCase,
  private val changePasswordUseCase: ChangePasswordUseCase,
  private val changeAvatarUseCase: ChangeAvatarUseCase,
  private val getUserDataUseCase: GetUserDataUseCase,
  private val saveUserUseCase: SaveUserUseCase,
  private val toggleTwoFactorAuthenticationUseCase: ToggleTwoFactorAuthenticationUseCase,
  private val updateUserUseCase: UpdateUserUseCase
  ) : ViewModel() {

  val user = getUserUseCase()
  private lateinit var sessionExpiredListener: SessionExpiredListener
  private val _passwordRequestCode = MutableLiveData<ResponseUI<Any>>()

  val passwordRequestCode: LiveData<ResponseUI<Any>>
    get() = _passwordRequestCode

  private val _userUpdateResponse = MutableLiveData<ResponseUI<Any>>()

  val userUpdateResponse: LiveData<ResponseUI<Any>>
    get() = _userUpdateResponse

  private val _toggleTwoFactorAuthResponse = MutableLiveData<ResponseUI<Any>>()

  val toggleTwoFactorAuthResponse: LiveData<ResponseUI<Any>>
    get() = _toggleTwoFactorAuthResponse

  private val _changeAvatarResponse = MutableLiveData<ResponseUI<Any>>()

  val changeAvatarResponse: LiveData<ResponseUI<Any>>
    get() = _changeAvatarResponse

  fun changePassword(password: String) {

    viewModelScope.launch {
      try {
        changePasswordUseCase(password)
        _passwordRequestCode.value = ResponseUI.success()
      } catch (e: ApiException) {
        _passwordRequestCode.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _passwordRequestCode.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _passwordRequestCode.value = ResponseUI.error(e.message)
      }
    }
  }

  fun changeAvatar(
    url: String,
    user: User
  ) {
    viewModelScope.launch {
      try {
        changeAvatarUseCase(url)
        val authResponse = getUserDataUseCase()
        Timber.d(authResponse.toString())
        authResponse.let {
          val mUser = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL,
            it.crypto, it.twoFactorAuthentication, user.authToken,
            user.authTokenExpiresOn, user.refreshToken, user.refreshTokenExpiresOn,
            user.trustedDevice
          )
          saveUserUseCase(user)
        }
        _changeAvatarResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _changeAvatarResponse.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _changeAvatarResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _changeAvatarResponse.value = ResponseUI.error(e.message)
      }
    }
  }

  fun toggleTwoFactorAuth() {
    viewModelScope.launch {
      try {
        toggleTwoFactorAuthenticationUseCase()
        _toggleTwoFactorAuthResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message)
      }
    }
  }

  fun updateUser(
    user: User
  ) {
    viewModelScope.launch {
      try {
        val updateUserDto = UpdateUserDto(
          identifier = user.username ?: "",
          email = user.email ?: "",
          firstName = user.firstName,
          lastName = user.lastName,
          avatarURL = user.avatarURL,
          twoFactorAuthentication = user.twoFactorAuthentication,
          authToken = AuthToken(user.authToken, user.authTokenExpiresOn),
          refreshToken = AuthToken(user.refreshToken, user.refreshTokenExpiresOn)
        )
        updateUserUseCase(updateUserDto)
        saveUserUseCase(user)
        _userUpdateResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _userUpdateResponse.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _userUpdateResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _userUpdateResponse.value = ResponseUI.error(e.message)
      }
    }
  }
}
