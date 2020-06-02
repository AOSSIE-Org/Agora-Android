package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.android.material.textfield.TextInputLayout
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.remote.RetrofitClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

internal class InviteVotersViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private var loadToast: LoadToast? = null
  var inviteVoterListener: InviteVoterListener? = null

  @Throws(
      JSONException::class
  ) fun inviteVoters(
    mVoterNames: ArrayList<String>,
    mVoterEmails: ArrayList<String>,
    id: String,
    token: String
  ) {
    loadToast = LoadToast(context)
    loadToast!!.setText("Inviting Voters")
    loadToast!!.show()
    val jsonArray = JSONArray()
    for (i in mVoterEmails.indices) {
      val jsonObject = JSONObject()
      jsonObject.put("name", mVoterNames[i])
      jsonObject.put("email", mVoterEmails[i])
      jsonArray.put(jsonObject)
      Log.d("TAG", "inviteVoters: $jsonArray")
      sendVoters(id, token, jsonArray)
    }
  }

  fun inviteValidator(
    email: String,
    name: String,
    mVoterEmails: ArrayList<String>
  ): Boolean {
    val isNameValid = nameValidator(name)
    val isEmailValid = emailValidator(email, mVoterEmails)
    return if (isNameValid && isEmailValid) {
      true
    } else false
  }

  fun emailValidator(
    email: String,
    mVoterEmails: ArrayList<String>
  ): Boolean {
    val base = context as Activity
    if (email.isEmpty()) {
      (base.findViewById<View>(
          id.text_input_voter_email
      ) as TextInputLayout).error = "Please enter Voter's Email"
      return false
    } else if (mVoterEmails.contains(email)) {
      (base.findViewById<View>(
          id.text_input_voter_email
      ) as TextInputLayout).error = base.resources
          .getString(string.voter_same_email)
      return false
    }
    (base.findViewById<View>(
        id.text_input_voter_email
    ) as TextInputLayout).error = null
    return true
  }

  fun nameValidator(name: String): Boolean {
    val base = context as Activity
    if (name.isEmpty()) {
      (base.findViewById<View>(
          id.text_input_voter_name
      ) as TextInputLayout).error = "Please enter Voter's Name"
      return false
    }
    (base.findViewById<View>(
        id.text_input_voter_name
    ) as TextInputLayout).error = null
    return true
  }

  private fun sendVoters(
    id: String,
    token: String,
    jsonArray: JSONArray
  ) {
    val apiService = RetrofitClient.getAPIService()
    val sendVotersResponse =
      apiService.sendVoters(token, id, jsonArray.toString())
    sendVotersResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          loadToast!!.success()
          Toast.makeText(context, "Voters Added Successfully", Toast.LENGTH_SHORT)
              .show()
          inviteVoterListener!!.onSendInviteSuccess()
        } else if (response.message() == "Internal Server Error") {
          loadToast!!.error()
          Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT)
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

}