package org.aossie.agoraandroid.ui.fragments.invitevoters

import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
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

  data class Response (val isSuccessful: Boolean=true, val msg: String="")

  @Throws(
      JSONException::class
  ) fun inviteVoters(
    mVoterNames: ArrayList<String>,
    mVoterEmails: ArrayList<String>,
    id: String
  ) {
    viewModelScope.launch {
      val jsonArray = JSONArray()
      var response = Response()
      for (i in mVoterEmails.indices) {
        val jsonObject = JSONObject()
        jsonObject.put("name", mVoterNames[i])
        jsonObject.put("hash", mVoterEmails[i])
        jsonArray.put(jsonObject)
        Timber.tag("TAG").d("inviteVoters: $jsonArray")
        response = sendVoters(id, jsonArray.toString())
        if (!response.isSuccessful) {
          inviteVoterListener.onFailure(response.msg)
          return@launch
        }
      }
      inviteVoterListener.onSuccess(response.msg)
    }
  }

  private suspend fun sendVoters(
    id: String,
    body: String
  ): Response {
    inviteVoterListener.onStarted()
    return try {
      val response = electionsRepository.sendVoters(id, body)
      Timber.d(response.toString())
      Response(true, response[1])
    } catch (e: Exception) {
      Response(false, e.message!!)
    }
  }

}