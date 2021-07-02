package org.aossie.agoraandroid.ui.fragments.invitevoters

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {
  private val _getSendVoterLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSendVoterLiveData = _getSendVoterLiveData
  lateinit var sessionExpiredListener: SessionExpiredListener

  fun inviteVoters(
    mVoterNames: ArrayList<String>,
    mVoterEmails: ArrayList<String>,
    id: String
  ) {

    val votersData = mutableListOf<VotersDto>()
    for (i in mVoterEmails.indices)
      votersData.add(VotersDto(mVoterNames[i], mVoterEmails[i]))

    sendVoters(id, votersData)
  }

  private fun sendVoters(
    id: String,
    body: List<VotersDto>
  ) {
    _getSendVoterLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val response = electionsRepository.sendVoters(id, body)
        Timber.d(response.toString())
        _getSendVoterLiveData.value = ResponseUI.success(response[1])
      } catch (e: ApiException) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message ?: "")
      } catch (e: Exception) {
        _getSendVoterLiveData.value = ResponseUI.error(e.message ?: "")
      }
    }
  }
}
