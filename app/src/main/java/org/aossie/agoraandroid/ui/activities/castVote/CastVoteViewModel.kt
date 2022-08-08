package org.aossie.agoraandroid.ui.activities.castVote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.data.Repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.network.dto.ElectionDto
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import javax.inject.Inject

class CastVoteViewModel
@Inject
constructor(
  private val castVoteActivityUseCases: CastVoteActivityUseCases
) : ViewModel() {

  private val mVerifyVoterResponse = MutableStateFlow<ResponseUI<Any>?>(null)

  val verifyVoterResponse: StateFlow<ResponseUI<Any>?>
    get() = mVerifyVoterResponse

  private val _getDeepLinkStateFlow = MutableStateFlow<ResponseUI<String>?>(null)

  val getDeepLinkStateFlow: StateFlow<ResponseUI<String>?>
    get() = _getDeepLinkStateFlow

  private val mCastVoteResponse = MutableStateFlow<ResponseUI<Any>?>(null)

  val castVoteResponse: StateFlow<ResponseUI<Any>?>
    get() = mCastVoteResponse

  private val mElection = MutableStateFlow<ElectionDtoModel?>(null)

  val election: StateFlow<ElectionDtoModel?>
    get() = mElection

  fun verifyVoter(id: String) {
    viewModelScope.launch {
      try {
        val electionDtoModel = castVoteActivityUseCases.verifyVotersUseCase(id)
        electionDtoModel._id = id
        mVerifyVoterResponse.value = ResponseUI.success()
        mElection.value = electionDtoModel
      } catch (e: ApiException) {
        mVerifyVoterResponse.value = ResponseUI.error(e.message)
      } catch (e: NoInternetException) {
        mVerifyVoterResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        mVerifyVoterResponse.value = ResponseUI.error(e.message)
      }
    }
  }

  fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ) {
    viewModelScope.launch {
      try {
        castVoteActivityUseCases.castVoteUseCase(id, ballotInput, passCode)
        mCastVoteResponse.value = ResponseUI.success()
      } catch (e: ApiException) {
        mCastVoteResponse.value = ResponseUI.error(e.message)
      } catch (e: NoInternetException) {
        mCastVoteResponse.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        mCastVoteResponse.value = ResponseUI.error(e.message)
      }
    }
  }

  fun getResolvedPath(encodedURL: String) {
    _getDeepLinkStateFlow.value = ResponseUI.loading()
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val originalURL = URL(encodedURL)
        val con: HttpURLConnection = originalURL.openConnection() as HttpURLConnection
        con.instanceFollowRedirects = false
        val resolvedURL = URL(con.getHeaderField("Location"))
        withContext(Dispatchers.Main) {
          _getDeepLinkStateFlow.value = ResponseUI.success(resolvedURL.path.toString())
        }
      } catch (ex: MalformedURLException) {
        withContext(Dispatchers.Main) {
          _getDeepLinkStateFlow.value = ResponseUI.error("")
        }
      } catch (ex: Exception) {
        withContext(Dispatchers.Main) {
          _getDeepLinkStateFlow.value = ResponseUI.error(ex.message)
        }
      }
    }
  }
}
