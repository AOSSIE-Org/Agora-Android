package org.aossie.agoraandroid.ui.fragments.createelection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import javax.inject.Inject

internal class CreateElectionViewModel
@Inject
constructor(
  private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs,
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val _getCreateElectionData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getCreateElectionData = _getCreateElectionData

  fun createElection() {
    _getCreateElectionData.value = ResponseUI.loading()
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val response = electionsRepository.createElection(
          ElectionDto(
            listOf(), electionDetailsSharedPrefs.ballotVisibility,
            electionDetailsSharedPrefs.getCandidates(),
            electionDetailsSharedPrefs.electionDesc,
            "Election",
            electionDetailsSharedPrefs.endTime,
            electionDetailsSharedPrefs.isInvite,
            electionDetailsSharedPrefs.isRealTime,
            electionDetailsSharedPrefs.electionName,
            1,
            electionDetailsSharedPrefs.startTime,
            electionDetailsSharedPrefs.voterListVisibility,
            electionDetailsSharedPrefs.votingAlgo
          )
        )
        _getCreateElectionData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
       _getCreateElectionData.value = ResponseUI.error(e.message?:"")
      } catch (e: NoInternetException) {
       _getCreateElectionData.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
       _getCreateElectionData.value = ResponseUI.error(e.message?:"")
      }
    }
  }
}
