package org.aossie.agoraandroid.ui.fragments.createelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

internal class CreateElectionViewModel
@Inject
constructor(
    private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs,
    private val prefs: PreferenceProvider,
    private val electionsRepository: ElectionsRepository
) : ViewModel() {

  lateinit var createElectionListener: CreateElectionListener

  fun createElection() {
    createElectionListener.onStarted()
    val candidates = electionDetailsSharedPrefs.getCandidates()
    val jsArray = JSONArray(candidates)
    val token = prefs.getCurrentToken()
    val jsonObject = JSONObject()
    try {
      val ballot = JSONArray(ArrayList<Ballot>())
      jsonObject.put("ballot", ballot)
      jsonObject.put("name", electionDetailsSharedPrefs.electionName)
      jsonObject.put("description", electionDetailsSharedPrefs.electionDesc)
      jsonObject.put("voterListVisibility", electionDetailsSharedPrefs.voterListVisibility)
      jsonObject.put("startingDate", electionDetailsSharedPrefs.startTime)
      jsonObject.put("endingDate", electionDetailsSharedPrefs.endTime)
      jsonObject.put("isInvite", electionDetailsSharedPrefs.isInvite)
      jsonObject.put("ballotVisibility", electionDetailsSharedPrefs.ballotVisibility)
      jsonObject.put("isRealTime", electionDetailsSharedPrefs.isRealTime)
      jsonObject.put("votingAlgo", electionDetailsSharedPrefs.votingAlgo)
      jsonObject.put("candidates", jsArray)
      jsonObject.put("noVacancies", 1)
      jsonObject.put("electionType", "Election")
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    viewModelScope.launch(Dispatchers.Main){
      try{
        val response = electionsRepository.createElection(token!!, jsonObject.toString())
         createElectionListener.onSuccess(response[1])
      }catch (e : ApiException){
        createElectionListener.onFailure(e.message!!)
      }catch (e : NoInternetException){
        createElectionListener.onFailure(e.message!!)
      }catch (e : Exception){
        createElectionListener.onFailure(e.message!!)
      }
    }
  }

}