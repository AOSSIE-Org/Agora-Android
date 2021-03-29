package org.aossie.agoraandroid.ui.fragments.invitevoters

import timber.log.Timber
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
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
    id: String
  ) {
    val jsonArray = JSONArray()
    for (i in mVoterEmails.indices) {
      val jsonObject = JSONObject()
      jsonObject.put("name", mVoterNames[i])
      jsonObject.put("hash", mVoterEmails[i])
      jsonArray.put(jsonObject)
      Timber.tag("TAG").d("inviteVoters: $jsonArray")
      sendVoters(id, jsonArray.toString())
    }
  }

  private fun sendVoters(
    id: String,
    body: String
  ) {
    inviteVoterListener.onStarted()
   viewModelScope.launch {
      try {
        val response = electionsRepository.sendVoters(id, body)
        Timber.d(response.toString())
        inviteVoterListener.onSuccess(response[1])
      } catch (e: ApiException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        if (e.message.toString()
                .toBoolean()
        ) sendVoters(id, body)
        else inviteVoterListener.onSessionExpired()
      } catch (e: NoInternetException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: Exception) {
        inviteVoterListener.onFailure(e.message!!)
      }
    }
  }

}