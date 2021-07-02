package org.aossie.agoraandroid.ui.fragments.electionDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.data.dto.WinnerDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class ElectionDetailsViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val _getVoterResponseLiveData = MutableLiveData<ResponseUI<VotersDto>>()
  var getVoterResponseLiveData: LiveData<ResponseUI<VotersDto>> = _getVoterResponseLiveData
  private val mNotConnected = MutableLiveData<Boolean>()
  var notConnected: LiveData<Boolean> = mNotConnected
  private val _getBallotResponseLiveData = MutableLiveData<ResponseUI<BallotDto>>()
  var getBallotResponseLiveData: LiveData<ResponseUI<BallotDto>> = _getBallotResponseLiveData
  private val _getResultResponseLiveData = MutableLiveData<ResponseUI<WinnerDto>>()
  var getResultResponseLiveData: LiveData<ResponseUI<WinnerDto>> = _getResultResponseLiveData
  private val _getDeleteElectionLiveData = MutableLiveData<ResponseUI<WinnerDto>>()
  var getDeleteElectionLiveData: LiveData<ResponseUI<WinnerDto>> = _getDeleteElectionLiveData

  lateinit var sessionExpiredListener: SessionExpiredListener

  suspend fun getElectionById(id: String): LiveData<Election> {
    return electionsRepository.getElectionById(id)
  }

  fun getBallot(
    id: String?
  ) {
    _getBallotResponseLiveData.value = ResponseUI.loading()
    Coroutines.main {
      try {
        val response: List<BallotDto> = electionsRepository.getBallots(id!!).ballots
        Timber.d(response.toString())
        _getBallotResponseLiveData.value = ResponseUI.success(response)
      } catch (e: ApiException) {
        _getBallotResponseLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        _getBallotResponseLiveData.value = ResponseUI.error(e.message ?: "")
      }
    }
  }

  fun getVoter(
    id: String?
  ) {
    _getVoterResponseLiveData.value = ResponseUI.loading()
    Coroutines.main {
      try {
        val response = electionsRepository.getVoters(id!!)
        Timber.d(response.toString())
        _getVoterResponseLiveData.value = ResponseUI.success(response)
      } catch (e: ApiException) {
        _getVoterResponseLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
      } catch (e: Exception) {
        _getVoterResponseLiveData.value = ResponseUI.error(e.message ?: "")
      }
    }
  }

  fun deleteElection(
    id: String?
  ) {
    _getDeleteElectionLiveData.value = ResponseUI.loading()
    Coroutines.main {
      try {
        val response = electionsRepository.deleteElection(id!!)
        Timber.d(response.toString())
        _getDeleteElectionLiveData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: Exception) {
        _getDeleteElectionLiveData.value = ResponseUI.error(e.message ?: "")
      }
    }
  }

  fun getResult(
    id: String?
  ) {
    _getResultResponseLiveData.value = ResponseUI.loading()
    Coroutines.main {
      try {
        val response = electionsRepository.getResult(id!!)
        if (!response.isNullOrEmpty())
          _getResultResponseLiveData.value = ResponseUI.success(response[0])
        else
          _getResultResponseLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getResultResponseLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        mNotConnected.postValue(true)
        _getResultResponseLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: Exception) {
        _getResultResponseLiveData.value = ResponseUI.error(e.message ?: "")
      }
    }
  }
}
