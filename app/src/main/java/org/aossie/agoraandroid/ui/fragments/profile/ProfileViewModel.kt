package org.aossie.agoraandroid.ui.fragments.profile

import timber.log.Timber
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.SessionExpired
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val userRepository: UserRepository
) : ViewModel() {

  val user = userRepository.getUser()

  private val _passwordRequestCode = MutableLiveData<ResponseResults>()

  val passwordRequestCode: LiveData<ResponseResults>
    get() = _passwordRequestCode

  private val _userUpdateResponse = MutableLiveData<ResponseResults>()

  val userUpdateResponse: LiveData<ResponseResults>
    get() = _userUpdateResponse

  private val _toggleTwoFactorAuthResponse = MutableLiveData<ResponseResults>()

  val toggleTwoFactorAuthResponse: LiveData<ResponseResults>
    get() = _toggleTwoFactorAuthResponse

  private val _changeAvatarResponse = MutableLiveData<ResponseResults>()

  val changeAvatarResponse: LiveData<ResponseResults>
    get() = _changeAvatarResponse

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }
    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
    object SessionExpired : ResponseResults()
  }

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
        if (e.message.toString()
                .toBoolean()
        ) changePassword(password)
        else _passwordRequestCode.value = SessionExpired
      }catch (e: NoInternetException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: Exception) {
        _passwordRequestCode.value = Error(e.message.toString())
      }
    }
  }

  fun changeAvatar(
    url: String,
    user: User) {
    val jsonObject = JSONObject()
    try {
      jsonObject.put("url", url)
      Timber.tag("change avatar").d(jsonObject.toString())
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
        if (e.message.toString()
                .toBoolean()
        ) changeAvatar(url, user)
        else _changeAvatarResponse.value = SessionExpired
      }catch (e: NoInternetException) {
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
        if (e.message.toString()
                .toBoolean()
        ) toggleTwoFactorAuth()
        else _toggleTwoFactorAuthResponse.value = SessionExpired
      }catch (e: NoInternetException) {
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
        if (e.message.toString()
                .toBoolean()
        ) updateUser(user)
        else _userUpdateResponse.value = SessionExpired
      }catch (e: NoInternetException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _userUpdateResponse.value = Error(e.message.toString())
      }
    }
  }
}