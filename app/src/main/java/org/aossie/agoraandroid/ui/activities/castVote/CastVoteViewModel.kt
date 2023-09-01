package org.aossie.agoraandroid.ui.activities.castVote

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.useCases.castVoteActivity.CastVoteActivityUseCases
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsViewModel.UiEvents
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
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

  private val _getDeepLinkStateFlow = MutableStateFlow<ResponseUI<String>?>(null)

  val getDeepLinkStateFlow: StateFlow<ResponseUI<String>?>
    get() = _getDeepLinkStateFlow

  private val mElection = MutableStateFlow<ElectionDtoModel?>(null)

  val election: StateFlow<ElectionDtoModel?>
    get() = mElection

  private val _progressAndErrorState = mutableStateOf(ScreensState())
  val progressAndErrorState: State<ScreensState> = _progressAndErrorState

  private val _verifiedVoter = MutableStateFlow(false)
  val verifiedVoter = _verifiedVoter.asStateFlow()

  private val _uiEventsFlow = MutableSharedFlow<UiEvents>()
  val uiEventsFlow = _uiEventsFlow.asSharedFlow()

  fun verifyVoter(id: String) {
    _verifiedVoter.value = false
    viewModelScope.launch {
      try {
        val electionDtoModel = castVoteActivityUseCases.verifyVotersUseCase(id)
        electionDtoModel._id = id
        _verifiedVoter.value = true
        mElection.value = electionDtoModel
      } catch (e: ApiException) {
        showMessage(e.message!!)
        delay(2000)
        _uiEventsFlow.emit(UiEvents.VerifyVoterError)
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
        delay(2000)
        _uiEventsFlow.emit(UiEvents.VerifyVoterError)
      } catch (e: Exception) {
        showMessage(e.message!!)
        delay(2000)
        _uiEventsFlow.emit(UiEvents.VerifyVoterError)
      }
    }
  }

  fun castVote(
    id: String,
    ballotInput: String,
    passCode: String
  ) {
    showLoading("Casting your vote...")
    viewModelScope.launch {
      try {
        castVoteActivityUseCases.castVoteUseCase(id, ballotInput, passCode)
        showMessage(R.string.vote_successful)
        delay(2000)
        _uiEventsFlow.emit(UiEvents.VoteCastSuccessFull)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
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

  private fun showLoading(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  fun showMessage(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair("",false)
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }

  sealed class UiEvents{
    object VoteCastSuccessFull:UiEvents()
    object VerifyVoterError:UiEvents()
  }
}
