package org.aossie.agoraandroid.ui.fragments.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.aossie.agoraandroid.utilities.lazyDeferred
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

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }
    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
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
        Log.d("friday", response[1])
        _passwordRequestCode.value = Success(response[1])
      } catch (e: ApiException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        _passwordRequestCode.value = Error(e.message.toString())
      }catch (e: NoInternetException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: Exception) {
        _passwordRequestCode.value = Error(e.message.toString())
      }
    }
  }

  fun toggleTwoFactorAuth() {
    Coroutines.main {
      try {
        val response = userRepository.toggleTwoFactorAuth()
        Log.d("friday", response[1])
        _toggleTwoFactorAuthResponse.value = Success(response[1])
      } catch (e: ApiException) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        _toggleTwoFactorAuthResponse.value = Error(e.message.toString())
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
      jsonObject.put("email", user.email)
      jsonObject.put("twoFactorAuthentication", user.twoFactorAuthentication)
      tokenObject.put("token", user.token)
      tokenObject.put("expiresOn", user.expiredAt)
      jsonObject.put("token", tokenObject)
      Log.d("update user", user.toString())
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.updateUser(jsonObject.toString())
        Log.d("friday", response[1])
        userRepository.saveUser(user)
        _userUpdateResponse.value = Success(response[1])
      } catch (e: ApiException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: SessionExpirationException) {
        Log.d("friday", "Session Expired")
        _userUpdateResponse.value = Error(e.message.toString())
      }catch (e: NoInternetException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _userUpdateResponse.value = Error(e.message.toString())
      }
    }
  }
}