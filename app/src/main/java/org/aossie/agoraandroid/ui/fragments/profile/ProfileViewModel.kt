package org.aossie.agoraandroid.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.network.responses.AuthToken
import org.aossie.agoraandroid.domain.model.UpdateUserDtoModel
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.domain.use_cases.profile.ProfileUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val profileUseCases: ProfileUseCases
) : ViewModel() {

  val user = profileUseCases.getUserUseCase()
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
        profileUseCases.changePasswordUseCase(password)
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
    user: UserModel
  ) {

    viewModelScope.launch {
      try {
        profileUseCases.changeAvatarUseCase(url)

        val authResponse = profileUseCases.getUserDataUseCase()
        Timber.d(authResponse.toString())
        authResponse.let {
          val mUser = UserModel(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL,
            it.crypto, it.twoFactorAuthentication, user.authToken,
            user.authTokenExpiresOn, user.refreshToken, user.refreshTokenExpiresOn,
            user.trustedDevice
          )
          profileUseCases.saveUserUseCase(mUser)
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
        profileUseCases.toggleTwoFactorAuthUseCase()
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
    user: UserModel
  ) {
    viewModelScope.launch {
      try {
        val updateUserDtoModel = UpdateUserDtoModel(
          identifier = user.username ?: "",
          email = user.email ?: "",
          firstName = user.firstName,
          lastName = user.lastName,
          avatarURL = user.avatarURL,
          twoFactorAuthentication = user.twoFactorAuthentication,
          authToken = AuthToken(user.authToken, user.authTokenExpiresOn),
          refreshToken = AuthToken(user.refreshToken, user.refreshTokenExpiresOn)
        )
        profileUseCases.updateUserUseCase(updateUserDtoModel)
        profileUseCases.saveUserUseCase(user)
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
