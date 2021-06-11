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
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  val user = userRepository.getUser()

  private val _passwordRequestCode = MutableLiveData<ResponseResult>()

  val passwordRequestCode: LiveData<ResponseResult>
    get() = _passwordRequestCode

  private val _userUpdateResponse = MutableLiveData<ResponseResult>()

  val userUpdateResponse: LiveData<ResponseResult>
    get() = _userUpdateResponse

  private val _toggleTwoFactorAuthResponse = MutableLiveData<ResponseResult>()

  val toggleTwoFactorAuthResponse: LiveData<ResponseResult>
    get() = _toggleTwoFactorAuthResponse

  private val _changeAvatarResponse = MutableLiveData<ResponseResult>()

  val changeAvatarResponse: LiveData<ResponseResult>
    get() = _changeAvatarResponse

  fun changePassword(password: String) {

    Coroutines.main {
      try {
        userRepository.changePassword(password)
        _passwordRequestCode.value = Success
      } catch (e: ApiException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: Exception) {
        _passwordRequestCode.value = Error(e.message.toString())
      }
    }
  }

  fun changeAvatar(
    url: String,
    user: User
  ) {

    Coroutines.main {
      try {
        val response = userRepository.changeAvatar(url)
        val authResponse = userRepository.getUserData()
        Timber.d(authResponse.toString())
        authResponse.let {
          val mUser = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL,
            it.crypto, it.twoFactorAuthentication, user.token,
            user.expiredAt, user.password, user.trustedDevice
          )
          userRepository.saveUser(mUser)
        }
        _changeAvatarResponse.value = Success
      } catch (e: ApiException) {
        _changeAvatarResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        _changeAvatarResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _changeAvatarResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _changeAvatarResponse.value = Error(e.message.toString())
      }
    }
  }

  fun toggleTwoFactorAuth() {
    Coroutines.main {
      try {
        userRepository.toggleTwoFactorAuth()
        _toggleTwoFactorAuthResponse.value = Success
      } catch (e: ApiException) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
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
          authToken = AuthToken(user.token, user.expiredAt)
        )
        userRepository.updateUser(updateUserDto)
        userRepository.saveUser(user)
        _userUpdateResponse.value = Success
      } catch (e: ApiException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        Timber.d("Session Expired")
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _userUpdateResponse.value = Error(e.message.toString())
      }
    }
  }
}
