package org.aossie.agoraandroid.ui.fragments.auth.signup

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.ui.activities.MainActivity
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class SignUpViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private var loadToast: LoadToast? = null
  fun signUpRequest(
    userName: String?,
    userPassword: String?,
    userEmail: String?,
    firstName: String?,
    lastName: String?,
    securityQuestion: String?,
    securityAnswer: String?
  ) {
    val jsonObject = JSONObject()
    val securityJsonObject = JSONObject()
    loadToast = LoadToast(context)
    loadToast!!.setText("Signing in")
    loadToast!!.show()
    try {
      jsonObject.put("identifier", userName)
      jsonObject.put("password", userPassword)
      jsonObject.put("email", userEmail)
      jsonObject.put("firstName", firstName)
      jsonObject.put("lastName", lastName)
      securityJsonObject.put("question", securityQuestion)
      securityJsonObject.put("answer", securityAnswer)
      jsonObject.put("securityQuestion", securityJsonObject)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService = RetrofitClient.getAPIService()
    val signUpResponse = apiService.createUser(jsonObject.toString())
    signUpResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "timeout") {
          loadToast!!.error()
          Toast.makeText(
              getApplication(),
              "Network Connection issues please try again",
              Toast.LENGTH_LONG
          )
              .show()
        }
        if (response.code() == 200) {
          loadToast!!.success()
          Toast.makeText(
              getApplication(),
              "An activation link has been sent to your email. Follow it to activate your account.",
              Toast.LENGTH_LONG
          )
              .show()
          context.startActivity(Intent(context, MainActivity::class.java))
        } else if (response.code() == 409) {
          loadToast!!.error()
          Toast.makeText(
              getApplication(), "User With Same UserName already Exists",
              Toast.LENGTH_LONG
          )
              .show()
        } else {
          loadToast!!.error()
          Toast.makeText(
              getApplication(), "Something went wrong please try again",
              Toast.LENGTH_LONG
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
            getApplication(), "" + t.message, Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

}