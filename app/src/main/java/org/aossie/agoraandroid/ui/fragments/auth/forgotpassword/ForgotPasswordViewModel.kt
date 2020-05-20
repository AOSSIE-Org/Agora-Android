package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private val mutableIsValidUsername =
    MutableLiveData<Boolean>()
  var isValidUsername: LiveData<Boolean> = mutableIsValidUsername
  private val mutableError = MutableLiveData<String>()
  var error: LiveData<String> = mutableError
  fun sendForgotPassLink(userName: String?) {
    val apiService = RetrofitClient.getAPIService()
    val forgotPasswordResponse =
      apiService.sendForgotPassword(userName)
    forgotPasswordResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == AppConstants.ok) {
          mutableIsValidUsername.setValue(true)
        } else {
          mutableError.value = getApplication<Application>().getString(string.invalid_username)
          mutableIsValidUsername.setValue(false)
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        mutableError.value = getApplication<Application>().getString(
            string.something_went_wrong_please_try_again_later
        )
      }
    })
  }

}