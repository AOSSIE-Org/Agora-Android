package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.util.Log
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
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
    Coroutines.main {
      try {
        val response = electionsRepository.sendVoters(token, id, body)
        Log.d("friday", response.toString())
        inviteVoterListener.onSuccess(response[1])
      }catch (e: ApiException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: Exception) {
        inviteVoterListener.onFailure(e.message!!)
      }
    }
  }

}