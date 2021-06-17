package org.aossie.agoraandroid.ui.activities.castVote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import javax.inject.Inject

class CastVoteViewModel
@Inject
constructor(
  val electionsRepository: ElectionsRepository,
  val userRepository: UserRepository
) : ViewModel() {

  private val mVerifyVoterResponse = MutableLiveData<ResponseResult>()

  val verifyVoterResponse: LiveData<ResponseResult>
    get() = mVerifyVoterResponse

  private val mCastVoteResponse = MutableLiveData<ResponseResult>()

  val castVoteResponse: LiveData<ResponseResult>
    get() = mCastVoteResponse

  private val mElection = MutableLiveData<ElectionDto>()

  val election: LiveData<ElectionDto>
    get() = mElection

  sealed class ResponseResults {
    class Success(text: String? = null) : ResponseResults() {
      val message = text
    }

    class Error(errorText: String) : ResponseResults() {
      val message = errorText
    }
  }

  fun verifyVoter(id: String) {
    try {
      Coroutines.main {
        val electionDto = electionsRepository.verifyVoter(id)
        electionDto._id = id
        mVerifyVoterResponse.value = Success
        mElection.value = electionDto
      }
    } catch (e: ApiException) {
      mVerifyVoterResponse.value = Error(e.message.toString())
    } catch (e: NoInternetException) {
      mVerifyVoterResponse.value = Error(e.message.toString())
    } catch (e: Exception) {
      mVerifyVoterResponse.value = Error(e.message.toString())
    }
  }

  fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ) {
    try {
      Coroutines.main {
        electionsRepository.castVote(id, ballotInput, passCode)
        mCastVoteResponse.value = Success
      }
    } catch (e: ApiException) {
      mCastVoteResponse.value = Error(e.message.toString())
    } catch (e: NoInternetException) {
      mCastVoteResponse.value = Error(e.message.toString())
    } catch (e: Exception) {
      mCastVoteResponse.value = Error(e.message.toString())
    }
  }

  fun deleteUserData() {
    Coroutines.main {
      userRepository.deleteUser()
    }
  }
}
