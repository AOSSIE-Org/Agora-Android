package org.aossie.agoraandroid.ui.activities.castVote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

class CastVoteViewModel
@Inject
constructor(
  val electionsRepository: ElectionsRepository
): ViewModel() {

  private val mVerifyVoterResponse = MutableLiveData<ResponseResults>()

  val verifyVoterResponse: LiveData<ResponseResults>
    get() = mVerifyVoterResponse

  private val mElection = MutableLiveData<ElectionResponse>()

  val election: LiveData<ElectionResponse>
    get() = mElection

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }
    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
  }

  fun verifyVoter(id: String, pass: String){
    try {
      Coroutines.main {
        val electionResponse = electionsRepository.verifyVoter(id, pass)
        electionResponse._id = id
        mVerifyVoterResponse.value = Success("Success")
        mElection.value = electionResponse
      }
    }catch (e: ApiException){
      mVerifyVoterResponse.value = Error(e.message.toString())
    }catch (e: NoInternetException){
      mVerifyVoterResponse.value = Error(e.message.toString())
    }catch (e: Exception){
      mVerifyVoterResponse.value = Error(e.message.toString())
    }
  }

}