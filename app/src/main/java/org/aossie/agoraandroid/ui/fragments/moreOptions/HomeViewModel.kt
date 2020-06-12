package org.aossie.agoraandroid.ui.fragments.moreOptions

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.lazyDeferred
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
  private val electionsRepository: ElectionsRepository,
  private val context: Context
) : ViewModel() {
  private val sharedPrefs = SharedPrefs(context)
  var authListener: AuthListener? = null

  val elections by lazyDeferred{
    electionsRepository.getElections()
  }

  fun doLogout(token: String?) {
    authListener!!.onStarted()
    val apiService = RetrofitClient.getAPIService()
    val logoutResponse = apiService.logout(token)
    logoutResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          Toast.makeText(
              context, "Logged Out Successfully",
              Toast.LENGTH_SHORT
          )
              .show()
          sharedPrefs.clearLogin()
          authListener!!.onSuccess()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        authListener!!.onFailure("Something Went Wrong Please Try Again Later")
      }
    })
  }

}