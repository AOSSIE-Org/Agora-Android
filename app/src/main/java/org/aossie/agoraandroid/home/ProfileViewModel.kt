package org.aossie.agoraandroid.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

  private val _passwordRequestCode = MutableLiveData<Int>()

  val passwordRequestCode: LiveData<Int>
    get() = _passwordRequestCode

  private val _updateUserRequestCode = MutableLiveData<Int>()

  val updateUserRequestCode: LiveData<Int>
    get() = _updateUserRequestCode

  fun changePassword(
    newPass: String,
    confirmNewPass: String
  ) {
    if (newPass.isBlank())
      _passwordRequestCode.value = 1
    else if (confirmNewPass.isBlank())
      _passwordRequestCode.value = 2
    else if (!newPass.equals(confirmNewPass))
      _passwordRequestCode.value = 3
    else if (newPass.equals(pass))
      _passwordRequestCode.value = 4
    else {
      doChangePasswordRequest(newPass, token!!)
    }

  }

  private fun doChangePasswordRequest(
    password: String,
    token: String
  ) {
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
          _passwordRequestCode.value = 200
        } else {
          Log.d("TAG", "onResponse:" + response.body())
          _passwordRequestCode.value = 201
        }
      }

      override fun onFailure(
        call: Call<String>,
        t: Throwable
      ) {
        _passwordRequestCode.value = 500
      }
    })
  }

  fun updateUser(
    firstName : String,
    lastName : String,
    email : String,
    username : String
  ){
    if(firstName.isEmpty()){
      _updateUserRequestCode.value = 1
    }else if(lastName.isEmpty()){
      _updateUserRequestCode.value = 2
    }else if(email != this.email){
      _updateUserRequestCode.value = 3
    }else if(username != this.userName){
      _updateUserRequestCode.value = 4
    }else{
      doUpdateUserRequest(firstName, lastName, email, username)
    }

  }

  private fun doUpdateUserRequest(
    firstName : String,
    lastName : String,
    email : String,
    username : String
  ) {
    val jsonObject = JSONObject()
    val tokenObject = JSONObject()
    try{
      jsonObject.put("username", username)
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
          _updateUserRequestCode.value = 200
          sharedPrefs.saveFirstName(firstName)
          sharedPrefs.saveLastName(lastName)
          sharedPrefs.saveEmail(email)
          sharedPrefs.saveUserName(username)
        }else{
          _updateUserRequestCode.value = 201
          Log.i("Response", response.toString())
        }
      }

      override fun onFailure(
        call: Call<String>,
        t: Throwable
      ) {
        _updateUserRequestCode.value = 500
      }
    })
  }
}