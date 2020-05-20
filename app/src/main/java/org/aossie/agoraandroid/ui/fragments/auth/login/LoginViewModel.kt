package org.aossie.agoraandroid.ui.fragments.auth.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.home.HomeActivity
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private val sharedPrefs = SharedPrefs(getApplication())
  private var loadToast: LoadToast? = null
  fun logInRequest(
    userName: String?,
    userPassword: String?
  ) {
    loadToast = LoadToast(context)
    loadToast!!.setText("Logging in")
    loadToast!!.show()
    val jsonObject = JSONObject()
    try {
      jsonObject.put("identifier", userName)
      jsonObject.put("password", userPassword)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService = RetrofitClient.getAPIService()
    val logInResponse = apiService.logIn(jsonObject.toString())
    logInResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          try {
            val jsonObjects = JSONObject(response.body()!!)
            val token = jsonObjects.getJSONObject("token")
            val expiresOn = token.getString("expiresOn")
            val key = token.getString("token")
            val sUserName = jsonObjects.getString("username")
            val email = jsonObjects.getString("email")
            val firstName = jsonObjects.getString("firstName")
            val lastName = jsonObjects.getString("lastName")
            sharedPrefs.saveUserName(sUserName)
            sharedPrefs.saveEmail(email)
            sharedPrefs.saveFirstName(firstName)
            sharedPrefs.saveLastName(lastName)
            sharedPrefs.saveToken(key)
            sharedPrefs.savePass(userPassword)
            sharedPrefs.saveTokenExpiresOn(expiresOn)
            loadToast!!.success()
            val intent = Intent(context, HomeActivity::class.java)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        } else {
          loadToast!!.error()
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
        loadToast!!.error()
        Toast.makeText(
            getApplication(), "Something went wrong please try again",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
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
            context.startActivity(Intent(context, HomeActivity::class.java))
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