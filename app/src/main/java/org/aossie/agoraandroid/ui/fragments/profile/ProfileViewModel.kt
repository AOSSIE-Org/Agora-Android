package org.aossie.agoraandroid.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.dto.UpdateUserDto
import org.aossie.agoraandroid.data.network.responses.AuthToken
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.SessionExpired
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  val user = userRepository.getUser()
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

    Coroutines.main {
      try {
        userRepository.changePassword(password)
        _passwordRequestCode.value = ResponseUI.success()
      } catch (e: ApiException) {
        _passwordRequestCode.value = ResponseUI.error(e.message?:"")
      } catch (e: SessionExpirationException) {
       sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _passwordRequestCode.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
        _passwordRequestCode.value = ResponseUI.error(e.message?:"")
      }
    }
  }

  fun changeAvatar(
    url: String,
    user: User
  ) {

    Coroutines.main {
      try {
        userRepository.changeAvatar(url)
        val authResponse = userRepository.getUserData()
        Timber.d(authResponse.toString())
        authResponse.let {
          val mUser = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL,
            it.crypto, it.twoFactorAuthentication, user.authToken,
            user.authTokenExpiresOn, user.refreshToken, user.refreshTokenExpiresOn,
            user.trustedDevice
          )
          userRepository.saveUser(mUser)
        }
        _changeAvatarResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _changeAvatarResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _changeAvatarResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
        _changeAvatarResponse.value = ResponseUI.error(e.message?:"")
      }
    }
  }

  fun toggleTwoFactorAuth() {
    Coroutines.main {
      try {
        userRepository.toggleTwoFactorAuth()
        _toggleTwoFactorAuthResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
        _toggleTwoFactorAuthResponse.value = ResponseUI.error(e.message?:"")
      }
    }
  }

  fun updateUser(
    user: User
  ) {
    Coroutines.main {
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
        userRepository.updateUser(updateUserDto)
        userRepository.saveUser(user)
        _userUpdateResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        _userUpdateResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _userUpdateResponse.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
        _userUpdateResponse.value = ResponseUI.error(e.message?:"")
      }
    }
  }
}
