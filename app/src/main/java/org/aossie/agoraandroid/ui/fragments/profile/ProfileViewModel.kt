package org.aossie.agoraandroid.ui.fragments.profile

import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber

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
    val jsonObject = JSONObject()
    try {
      jsonObject.put("password", password)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.changePassword(jsonObject.toString())
        Timber.d(response[1])
        _passwordRequestCode.value = Success(response[1])
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
    val jsonObject = JSONObject()
    try {
      jsonObject.put("url", url)
      Timber.tag("change avatar")
        .d(jsonObject.toString())
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.changeAvatar(jsonObject.toString())
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
        Timber.d(response.toString())
        _changeAvatarResponse.value = Success(response[1])
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
        val response = userRepository.toggleTwoFactorAuth()
        Timber.d(response[1])
        _toggleTwoFactorAuthResponse.value = Success(response[1])
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
    val jsonObject = JSONObject()
    val tokenObject = JSONObject()
    try {
      jsonObject.put("username", user.username)
      jsonObject.put("firstName", user.firstName)
      jsonObject.put("lastName", user.lastName)
      jsonObject.put("avatarURL", user.avatarURL)
      jsonObject.put("email", user.email)
      jsonObject.put("twoFactorAuthentication", user.twoFactorAuthentication)
      tokenObject.put("token", user.token)
      tokenObject.put("expiresOn", user.expiredAt)
      jsonObject.put("token", tokenObject)
      Timber.d(user.toString())
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.updateUser(jsonObject.toString())
        Timber.d(response[1])
        userRepository.saveUser(user)
        _userUpdateResponse.value = Success(response[1])
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
