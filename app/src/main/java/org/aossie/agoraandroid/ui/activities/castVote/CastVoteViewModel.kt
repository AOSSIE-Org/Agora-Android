package org.aossie.agoraandroid.ui.activities.castVote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.network.responses.ElectionResponse
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.SessionExpired
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.ResponseResults.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import javax.inject.Inject

class CastVoteViewModel
@Inject
constructor(
  val electionsRepository: ElectionsRepository,
  val userRepository: UserRepository
): ViewModel() {

  private val mVerifyVoterResponse = MutableLiveData<ResponseResults>()

  val verifyVoterResponse: LiveData<ResponseResults>
    get() = mVerifyVoterResponse

  private val mCastVoteResponse = MutableLiveData<ResponseResults>()

  val castVoteResponse: LiveData<ResponseResults>
    get() = mCastVoteResponse

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

    object SessionExpired : ResponseResults()
  }

  fun verifyVoter(id: String){
    try {
      Coroutines.main {
        val electionResponse = electionsRepository.verifyVoter(id)
        electionResponse._id = id
        mVerifyVoterResponse.value = Success("Success")
        mElection.value = electionResponse
      }
    }catch (e: ApiException){
      mVerifyVoterResponse.value = Error(e.message.toString())
    }catch (e: SessionExpirationException) {
      if (e.message.toString()
              .toBoolean()
      ) verifyVoter(id)
      else mVerifyVoterResponse.value = SessionExpired
    }catch (e: NoInternetException){
        mVerifyVoterResponse.value = Error(e.message.toString())
    }catch (e: Exception){
      mVerifyVoterResponse.value = Error(e.message.toString())
    }
  }

  fun castVote(id: String, ballotInput: String, passCode: String){
    try {
      Coroutines.main {
        val response = electionsRepository.castVote(id, ballotInput, passCode)
        mCastVoteResponse.value = Success(response[1])
      }
    }catch (e: ApiException){
      mCastVoteResponse.value = Error(e.message.toString())
    }catch (e: SessionExpirationException) {
      if (e.message.toString()
              .toBoolean()
      ) castVote(id, ballotInput, passCode)
      else mVerifyVoterResponse.value = SessionExpired
    }catch (e: NoInternetException){
      mCastVoteResponse.value = Error(e.message.toString())
    }catch (e: Exception){
      mCastVoteResponse.value = Error(e.message.toString())
    }
  }

  fun deleteUserData() {
    Coroutines.main {
      userRepository.deleteUser()
    }
  }
}