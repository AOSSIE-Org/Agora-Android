package org.aossie.agoraandroid.data.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.db.model.Winner
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.data.network.responses.Ballots
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.data.network.responses.Voters
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class ElectionsRepository
@Inject
constructor(
  private val api: Api,
  private val db: AppDatabase,
  private val prefs: PreferenceProvider
) : ApiRequest() {

  private val elections = MutableLiveData<List<Election>>()

  init {
    elections.observeForever {
      saveElections(it)
    }
  }

  suspend fun fetchAndSaveElections() {
    withContext(Dispatchers.IO) {
      fetchElections()
    }
  }

  fun getElections(): LiveData<List<Election>> {
    return db.getElectionDao()
      .getElections()
  }

  suspend fun getFinishedElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getFinishedElectionsCount(currentDate)
    }
  }

  suspend fun getPendingElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getPendingElectionsCount(currentDate)
    }
  }

  suspend fun getTotalElectionsCount(): LiveData<Int> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getTotalElectionsCount()
    }
  }

  suspend fun getActiveElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getActiveElectionsCount(currentDate)
    }
  }

  private fun saveElections(elections: List<Election>) {
    Coroutines.io {
      prefs.setUpdateNeeded(false)
      db.getElectionDao()
        .deleteAllElections()
      db.getElectionDao()
        .saveElections(elections)
    }
  }

  suspend fun getPendingElections(currentDate: String): LiveData<List<Election>> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getPendingElections(currentDate)
    }
  }

  private suspend fun fetchElections() {
    val isNeeded = prefs.getUpdateNeeded()
    if (isNeeded) {
      try {
        val response = apiRequest { api.getAllElections(prefs.getCurrentToken()) }
        elections.postValue(response.elections)
        Timber.d(isNeeded.toString())
        Timber.d(response.toString())
      } catch (e: NoInternetException) {
      } catch (e: ApiException) {
      } catch (e: SessionExpirationException) {
      } catch (e: IOException) {
      }
    }
  }

  suspend fun getFinishedElections(currentDate: String): LiveData<List<Election>> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getFinishedElections(currentDate)
    }
  }

  suspend fun getActiveElections(currentDate: String): LiveData<List<Election>> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getActiveElections(currentDate)
    }
  }

  suspend fun getElectionById(id: String): LiveData<Election> {
    return withContext(Dispatchers.IO) {
      db.getElectionDao()
        .getElectionById(id)
    }
  }

  suspend fun deleteElection(
    id: String
  ): ArrayList<String> {
    return apiRequest { api.deleteElection(prefs.getCurrentToken(), id) }
  }

  suspend fun getVoters(
    id: String
  ): Voters {
    return apiRequest { api.getVoters(prefs.getCurrentToken(), id) }
  }

  suspend fun getBallots(
    id: String
  ): Ballots {
    return apiRequest { api.getBallot(prefs.getCurrentToken(), id) }
  }

  suspend fun sendVoters(
    id: String,
    body: String
  ): ArrayList<String> {
    return apiRequest { api.sendVoters(prefs.getCurrentToken(), id, body) }
  }

  suspend fun createElection(
    body: String
  ): ArrayList<String> {
    return apiRequest { api.createElection(body, prefs.getCurrentToken()) }
  }

  suspend fun verifyVoter(
    id: String
  ): ElectionResponse {
    return apiRequest { api.verifyVoter(id) }
  }

  suspend fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ): ArrayList<String> {
    val jsonObject = JSONObject()
    try {
      jsonObject.put("ballotInput", ballotInput)
      jsonObject.put("passCode", passCode)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    return apiRequest { api.castVote(id, jsonObject.toString()) }
  }

  suspend fun getResult(
    id: String
  ): List<Winner>? {
    val response = api.getResult(prefs.getCurrentToken(), id)
    return if (response.message() == AppConstants.ok) {
      apiRequest { response }
    } else {
      null
    }
  }
}
