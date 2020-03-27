package org.aossie.agoraandroid.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

  val sharedPrefs = SharedPrefs(application.applicationContext)

  val firstName: String?
    get() = sharedPrefs.firstName

  val lastName: String?
    get() = sharedPrefs.lastName

  val userName: String?
    get() = sharedPrefs.userName

  val email: String?
    get() = sharedPrefs.email

  val token: String?
    get() = sharedPrefs.token

  val pass: String?
    get() = sharedPrefs.pass

  val expiresOn: String?
    get() = sharedPrefs.tokenExpiresOn

  private val _passwordRequestCode = MutableLiveData<ResponseResults>()

  val passwordRequestCode: LiveData<ResponseResults>
    get() = _passwordRequestCode

  private val _userUpdateResponse = MutableLiveData<ResponseResults>()

  val userUpdateResponse: LiveData<ResponseResults>
    get() = _userUpdateResponse

  sealed class ResponseResults{
    class Success : ResponseResults()
    class Error(errorText : String) : ResponseResults(){
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
    val apiService = RetrofitClient.getAPIService()
    val changePassResponse = apiService.changePassword(jsonObject.toString(), token)
    changePassResponse.enqueue(object : Callback<String> {
      override fun onResponse(
        call: Call<String>,
        response: Response<String>
      ) {
        if (response.message() == "OK") {
          sharedPrefs.savePass(password)
          _passwordRequestCode.value = ResponseResults.Success()
        } else {
          Log.d("TAG", "onResponse:" + response.body())
          _passwordRequestCode.value = ResponseResults.Error(getApplication<Application>().getString(string.token_expired))
        }
      }

      override fun onFailure(
        call: Call<String>,
        t: Throwable
      ) {
        _passwordRequestCode.value = ResponseResults.Error(getApplication<Application>().getString(string.something_went_wrong_please_try_again_later))
      }
    })
  }

  fun updateUser(
    firstName : String,
    lastName : String
  ) {
    val jsonObject = JSONObject()
    val tokenObject = JSONObject()
    try{
      jsonObject.put("username", userName)
      jsonObject.put("firstName", firstName)
      jsonObject.put("lastName", lastName)
      jsonObject.put("email", email)
      jsonObject.put("twoFactorAuthentication", false)
      tokenObject.put("token", token)
      tokenObject.put("expiresOn", expiresOn)
      jsonObject.put("token", tokenObject)
    }catch (e: JSONException){
      e.printStackTrace()
    }

    val apiService = RetrofitClient.getAPIService()
    val updateUserResponse = apiService.updateUser(token, jsonObject.toString())
    updateUserResponse.enqueue(object : Callback<String> {
      override fun onResponse(
        call: Call<String>,
        response: Response<String>
      ) {
        if(response.message() == "OK") {
          sharedPrefs.saveFirstName(firstName)
          sharedPrefs.saveLastName(lastName)
          _userUpdateResponse.value = ResponseResults.Success()
        }else{
          _userUpdateResponse.value = ResponseResults.Error(getApplication<Application>().getString(string.token_expired))
        }
      }

      override fun onFailure(
        call: Call<String>,
        t: Throwable
      ) {
        _userUpdateResponse.value = ResponseResults.Error(getApplication<Application>().getString(string.something_went_wrong_please_try_again_later))
      }
    })
  }
}