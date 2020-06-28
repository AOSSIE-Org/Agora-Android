package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository,
    private val context: Context
) : ViewModel() {
  lateinit var inviteVoterListener: InviteVoterListener

  @Throws(
      JSONException::class
  ) fun inviteVoters(
    mVoterNames: ArrayList<String>,
    mVoterEmails: ArrayList<String>,
    id: String,
    token: String
  ) {
    val jsonArray = JSONArray()
    for (i in mVoterEmails.indices) {
      val jsonObject = JSONObject()
      jsonObject.put("name", mVoterNames[i])
      jsonObject.put("hash", mVoterEmails[i])
      jsonArray.put(jsonObject)
      Log.d("TAG", "inviteVoters: $jsonArray")
      sendVoters(id, token, jsonArray.toString())
    }
  }

  private fun sendVoters(
    id: String,
    token: String,
    body: String
  ) {
    inviteVoterListener.onStarted()
    try {
      Coroutines.main {
        val response = electionsRepository.sendVoters(token, id, body)
        Log.d("friday", response.toString())
        inviteVoterListener.onSuccess(response[1])
      }
    }catch (e: ApiException){
      inviteVoterListener.onFailure(e.message)
    }catch (e: NoInternetException){
      inviteVoterListener.onFailure(e.message)
    }catch (e: Exception){
      inviteVoterListener.onFailure(e.message)
    }
//    val apiService = RetrofitClient.getAPIService()
//    val sendVotersResponse =
//      apiService.sendVoters(token, id, jsonArray.toString())
//    sendVotersResponse.enqueue(object : Callback<String?> {
//      override fun onResponse(
//        call: Call<String?>,
//        response: Response<String?>
//      ) {
//        if (response.message() == "OK") {
//          Toast.makeText(context, "Voters Added Successfully", Toast.LENGTH_SHORT)
//              .show()
//          inviteVoterListener!!.onSendInviteSuccess()
//        } else if (response.message() == "Internal Server Error") {
//          Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT)
//              .show()
//        }
//      }
//
//      override fun onFailure(
//        call: Call<String?>,
//        t: Throwable
//      ) {
//        Toast.makeText(
//            context, "Something went wrong please try again",
//            Toast.LENGTH_SHORT
//        )
//            .show()
//      }
//    })
  }

}