package org.aossie.agoraandroid.ui.fragments.auth.login

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
  private val userRepository: UserRepository,
  application: Application
) : AndroidViewModel(application) {
  private val sharedPrefs = SharedPrefs(getApplication())
  var authListener: AuthListener ?= null

  fun logInRequest(
      identifier: String,
      password: String
  ){
    authListener?.onStarted()
    if (identifier.isEmpty() || password.isEmpty()) {
      authListener?.onFailure("Invalid Email or Password")
      return
    }
    viewModelScope.launch(Dispatchers.Main){
      try{
        val authResponse = userRepository.userLogin(identifier, password)
        authResponse.let {
          val user = User(it.username, it.email, it.firstName, it.lastName, it.towFactorAuthentication, it.token?.token, it.token?.expiredOn)
          sharedPrefs.saveUserName(user.username)
          sharedPrefs.saveEmail(user.email)
          sharedPrefs.saveFirstName(user.firstName)
          sharedPrefs.saveLastName(user.lastName)
          sharedPrefs.saveToken(user.token)
          sharedPrefs.savePass(password)
          sharedPrefs.saveTokenExpiresOn(user.expiredAt)
          authListener?.onSuccess()
        }
      }catch (e : ApiException){
        authListener?.onFailure(e.message!!)
      }catch (e : NoInternetException){
        authListener?.onFailure(e.message!!)
      }catch (e : Exception){
        authListener?.onFailure(e.message!!)
      }
    }
  }

  fun facebookLogInRequest(accessToken: String?) {
    val apiService = RetrofitClient.getAPIService()
    val facebookLogInResponse = apiService.facebookLogin(accessToken)
    facebookLogInResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          try {
            val jsonObject = JSONObject(response.body())
            val expiresOn = jsonObject.getString("expiresOn")
            val authToken = jsonObject.getString("token")
            sharedPrefs.saveToken(authToken)
            sharedPrefs.saveTokenExpiresOn(expiresOn)
            getUserData(authToken)
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        } else {
          Toast.makeText(
              getApplication(), "Wrong User Name or Password",
              Toast.LENGTH_SHORT
          )
              .show()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        Toast.makeText(
            getApplication(), "Something went wrong please try again",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

  private fun getUserData(authToken: String) {
    val apiService = RetrofitClient.getAPIService()
    val getDataResponse = apiService.getUserData(authToken)
    getDataResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          try {
            val jsonObject = JSONObject(response.body())
            val UserName = jsonObject.getString("username")
            val email = jsonObject.getString("email")
            val firstName = jsonObject.getString("firstName")
            val lastName = jsonObject.getString("lastName")
            sharedPrefs.saveUserName(UserName)
            sharedPrefs.saveEmail(email)
            sharedPrefs.saveFirstName(firstName)
            sharedPrefs.saveLastName(lastName)
            authListener?.onSuccess()
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        } else {
          Toast.makeText(
              getApplication(), "Wrong User Name or Password",
              Toast.LENGTH_SHORT
          )
              .show()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        Toast.makeText(
            getApplication(), "Something went wrong please try again",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

}