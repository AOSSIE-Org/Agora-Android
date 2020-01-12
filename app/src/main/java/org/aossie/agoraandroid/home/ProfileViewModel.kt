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

  private val _requestCode = MutableLiveData<Int>()

  val error: LiveData<Int>
    get() = _requestCode

  fun changePassword(newPass: String, confirmNewPass: String) {
    if (newPass.isBlank() && confirmNewPass.isBlank())
      _requestCode.value = 1
    else if (newPass.isBlank())
      _requestCode.value = 2
    else if (confirmNewPass.isBlank())
      _requestCode.value = 3
    else if (!newPass.equals(confirmNewPass))
      _requestCode.value = 4
    else if (newPass.equals(pass))
      _requestCode.value = 5
    else {
      doChangePasswordRequest(newPass, token!!);
    }

  }

  private fun doChangePasswordRequest(password: String, token: String) {
    val jsonObject = JSONObject()
    try {
      jsonObject.put("password", password)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService = RetrofitClient.getAPIService()
    val changePassResponse = apiService.changePassword(jsonObject.toString(), token)
    changePassResponse.enqueue(object : Callback<String> {
      override fun onResponse(call: Call<String>, response: Response<String>) {
        if (response.message() == "OK") {
          _requestCode.value = 200
        } else {
          Log.d("TAG", "onResponse:" + response.body())
          _requestCode.value = 201
        }
      }

      override fun onFailure(call: Call<String>, t: Throwable) {
        _requestCode.value = 500
      }
    })
  }
}


