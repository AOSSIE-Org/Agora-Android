package org.aossie.agoraandroid.ui.activities.castVote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.dto.ElectionDto
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class CastVoteViewModel
@Inject
constructor(
  val electionsRepository: ElectionsRepository,
  val userRepository: UserRepository
) : ViewModel() {

  lateinit var getResolvedPathListener: GetResolvedPathListener

  private val mVerifyVoterResponse = MutableLiveData<ResponseResult>()

  val verifyVoterResponse: LiveData<ResponseResult>
    get() = mVerifyVoterResponse

  private val mCastVoteResponse = MutableLiveData<ResponseResult>()

  val castVoteResponse: LiveData<ResponseResult>
    get() = mCastVoteResponse

  private val mElection = MutableLiveData<ElectionDto>()

  val election: LiveData<ElectionDto>
    get() = mElection

  fun verifyVoter(id: String) {
    Coroutines.main {
      try {
        val electionDto = electionsRepository.verifyVoter(id)
        electionDto._id = id
        mVerifyVoterResponse.value = Success
        mElection.value = electionDto
      } catch (e: ApiException) {
        mVerifyVoterResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        mVerifyVoterResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mVerifyVoterResponse.value = Error(e.message.toString())
      }
    }
  }

  fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ) {
    Coroutines.main {
      try {
        electionsRepository.castVote(id, ballotInput, passCode)
        mCastVoteResponse.value = Success
      } catch (e: ApiException) {
        mCastVoteResponse.value = Error(e.message.toString())
      } catch (e: NoInternetException) {
        mCastVoteResponse.value = Error(e.message.toString())
      } catch (e: Exception) {
        mCastVoteResponse.value = Error(e.message.toString())
      }
    }
  }

  fun getResolvedPath(encodedURL: String) {
    getResolvedPathListener.onStarted()
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val originalURL = URL(encodedURL)
        val con: HttpURLConnection = originalURL.openConnection() as HttpURLConnection
        con.instanceFollowRedirects = false
        val resolvedURL = URL(con.getHeaderField("Location"))
        withContext(Dispatchers.Main) {
          getResolvedPathListener.onSuccess(resolvedURL.path.toString())
        }
      } catch (ex: MalformedURLException) {
        withContext(Dispatchers.Main) {
          getResolvedPathListener.onFailure()
        }
      } catch (ex: Exception) {
        withContext(Dispatchers.Main) {
          getResolvedPathListener.onFailure(ex.message)
        }
      }
    }
  }
}
