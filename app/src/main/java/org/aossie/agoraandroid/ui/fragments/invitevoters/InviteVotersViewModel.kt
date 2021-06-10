package org.aossie.agoraandroid.ui.fragments.invitevoters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import java.util.ArrayList
import javax.inject.Inject

internal class InviteVotersViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {
  lateinit var inviteVoterListener: InviteVoterListener

  fun inviteVoters(
    mVoterNames: ArrayList<String>,
    mVoterEmails: ArrayList<String>,
    id: String
  ) {

    for (i in mVoterEmails.indices)
      sendVoters(id, VotersDto(mVoterNames[i], mVoterEmails[i]))
  }

  private fun sendVoters(
    id: String,
    body: VotersDto
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
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        inviteVoterListener.onFailure(e.message!!)
      } catch (e: Exception) {
        inviteVoterListener.onFailure(e.message!!)
      }
    }
  }
}
