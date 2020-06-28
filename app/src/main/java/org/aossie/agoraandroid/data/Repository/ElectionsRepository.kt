package org.aossie.agoraandroid.data.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.data.db.AppDatabase
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.ApiRequest
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import java.io.IOException
import javax.inject.Inject

class ElectionsRepository
@Inject
constructor(
  private val api : Api,
  private val db : AppDatabase,
  private val prefs: PreferenceProvider
): ApiRequest() {

  private val elections = MutableLiveData<List<Election>>()

  init {
    elections.observeForever{
      saveElections(it)
    }
  }

  suspend fun getElections() : LiveData<List<Election>> {
    return withContext(Dispatchers.IO){
      fetchElections()
      db.getElectionDao().getElections()
    }
  }

  suspend fun getFinishedElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getFinishedElectionsCount(currentDate)
    }
  }

  suspend fun getPendingElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getPendingElectionsCount(currentDate)
    }
  }

  suspend fun getTotalElectionsCount(): LiveData<Int> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getTotalElectionsCount()
    }
  }

  suspend fun getActiveElectionsCount(currentDate: String): LiveData<Int> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getActiveElectionsCount(currentDate)
    }
  }

  private fun saveElections(elections : List<Election>) {
    Coroutines.io {
      prefs.setUpdateNeeded(false)
      db.getElectionDao().saveElections(elections)
    }
  }

  suspend fun getPendingElections(currentDate: String): List<Election> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getPendingElections(currentDate)
    }
  }

  private suspend fun fetchElections(){
    val isNeeded = prefs.getUpdateNeeded()
    if(isNeeded){
      try {
        val response = apiRequest { api.getAllElections(prefs.getCurrentToken()) }
        elections.postValue(response.elections)
        Log.d("friday", isNeeded.toString())
        Log.d("friday", response.toString())
      }catch (e: NoInternetException){

      }catch (e: ApiException){

      }catch (e: IOException){

      }
    }
  }

  suspend fun getFinishedElections(currentDate: String): List<Election> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getFinishedElections(currentDate)
    }
  }

  suspend fun getActiveElections(currentDate: String): List<Election> {
    return withContext(Dispatchers.IO){
      db.getElectionDao().getActiveElections(currentDate)
    }
  }
}