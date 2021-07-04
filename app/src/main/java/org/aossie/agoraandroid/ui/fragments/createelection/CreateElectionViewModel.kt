package org.aossie.agoraandroid.ui.fragments.createelection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

internal class CreateElectionViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  lateinit var createElectionListener: CreateElectionListener

  fun createElection(election: ElectionDto) {
    createElectionListener.onStarted()
    viewModelScope.launch(Dispatchers.Main) {
      try {
        val response = electionsRepository.createElection(election)
        createElectionListener.onSuccess(response[1])
      } catch (e: ApiException) {
        createElectionListener.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        createElectionListener.onFailure(e.message!!)
      } catch (e: Exception) {
        createElectionListener.onFailure(e.message!!)
      }
    }
  }
}
