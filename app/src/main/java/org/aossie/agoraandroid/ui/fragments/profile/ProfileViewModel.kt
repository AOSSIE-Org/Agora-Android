package org.aossie.agoraandroid.ui.fragments.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.lazyDeferred
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class ProfileViewModel
@Inject
constructor(
  private val userRepository: UserRepository,
  private val context: Context
) : ViewModel() {

  val user by lazyDeferred {
    userRepository.getUser()
  }

  private val _passwordRequestCode = MutableLiveData<ResponseResults>()

  val passwordRequestCode: LiveData<ResponseResults>
    get() = _passwordRequestCode

  private val _userUpdateResponse = MutableLiveData<ResponseResults>()

  val userUpdateResponse: LiveData<ResponseResults>
    get() = _userUpdateResponse

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }
    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
  }

  fun changePassword(password: String, token: String) {
    val jsonObject = JSONObject()
    try {
      jsonObject.put("password", password)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.changePassword(token, jsonObject.toString())
        Log.d("friday", response[1])
        _passwordRequestCode.value = Success(response[1])
      } catch (e: ApiException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _passwordRequestCode.value = Error(e.message.toString())
      } catch (e: Exception) {
        _passwordRequestCode.value = Error(e.message.toString())
      }
    }
  }

  fun updateUser(
    userName: String,
    userEmail: String,
    firstName: String,
    lastName: String,
    token: String,
    expiresOn: String
  ) {
    //authListener.onStarted()
    val jsonObject = JSONObject()
    val tokenObject = JSONObject()
    try {
      jsonObject.put("username", userName)
      jsonObject.put("firstName", firstName)
      jsonObject.put("lastName", lastName)
      jsonObject.put("email", userEmail)
      jsonObject.put("twoFactorAuthentication", false)
      tokenObject.put("token", token)
      tokenObject.put("expiresOn", expiresOn)
      jsonObject.put("token", tokenObject)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    Coroutines.main {
      try {
        val response = userRepository.updateUser(token, jsonObject.toString())
        Log.d("friday", response[1])
        _userUpdateResponse.value = Success(response[1])
      } catch (e: ApiException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        _userUpdateResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        _userUpdateResponse.value = Error(e.message.toString())
      }
    }
  }
}