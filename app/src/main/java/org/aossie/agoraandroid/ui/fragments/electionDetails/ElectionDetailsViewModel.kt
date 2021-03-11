package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.db.model.Ballot
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class ElectionDetailsViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val mVoterResponse = MutableLiveData<List<VoterList>>()
  var voterResponse: LiveData<List<VoterList>> = mVoterResponse
  private val mNotConnected = MutableLiveData<Boolean>()
  var notConnected: LiveData<Boolean> = mNotConnected
  private val mBallotResponse = MutableLiveData<List<Ballot>>()
  var ballotResponse: LiveData<List<Ballot>> = mBallotResponse

  lateinit var displayElectionListener: DisplayElectionListener

  suspend fun getElectionById(id: String): LiveData<Election> {
    return electionsRepository.getElectionById(id)
  }

  fun getBallot(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.getBallots(id!!).ballots
        Log.d("friday", response.toString())
        mBallotResponse.postValue(response)
        displayElectionListener.onSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun getVoter(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.getVoters(id!!).voters
        Log.d("friday", response.toString())
        mVoterResponse.postValue(response)
        displayElectionListener.onSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }

  fun deleteElection(
    id: String?
  ) {
    displayElectionListener.onStarted()
    Coroutines.main {
      try {
        val response = electionsRepository.deleteElection(id!!)
        Log.d("friday", response.toString())
        displayElectionListener.onSuccess(response[1])
        displayElectionListener.onDeleteElectionSuccess()
      } catch (e: ApiException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        displayElectionListener.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        displayElectionListener.onFailure(e.message!!)
      } catch (e: Exception) {
        displayElectionListener.onFailure(e.message!!)
      }
    }
  }
}