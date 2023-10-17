package org.aossie.agoraandroid.ui.activities.castVote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.UiEvents.VerifyVoterError
import org.aossie.agoraandroid.ui.activities.castVote.CastVoteViewModel.UiEvents.VoteCastSuccessFull
import org.aossie.agoraandroid.ui.activities.main.MainActivity
import org.aossie.agoraandroid.ui.screens.castVote.CastVoteScreen
import org.aossie.agoraandroid.ui.screens.castVote.CastVoteScreenEvent.CastVoteClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.InternetManager
import org.aossie.agoraandroid.utilities.ResponseUI
import javax.inject.Inject

class CastVoteActivity :
  AppCompatActivity() {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private var passCode: String? = null
  private var id: String? = null

  @Inject
  lateinit var prefs: PreferenceProvider

  @Inject
  lateinit var internetManager: InternetManager

  private val viewModel: CastVoteViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    (application as AgoraApp).appComponent.inject(this)
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
    window.clearFlags(LayoutParams.FLAG_TRANSLUCENT_STATUS)

    setContent {
      val progressErrorState by viewModel.progressAndErrorState
      val electionDetails by viewModel.election.collectAsState()

      LaunchedEffect(key1 = viewModel) {
        viewModel.uiEventsFlow.collectLatest {
          when(it) {
            VoteCastSuccessFull -> {
              navigateToMainActivity(getString(string.vote_successful))
            }
            VerifyVoterError -> {
              navigateToMainActivity(
                getString(string.something_went_wrong_please_try_again_later)
              )
            }
          }
        }
      }

      AgoraTheme() {
        CastVoteScreen(
          screenState = progressErrorState,
          electionDetails = electionDetails
        ) {
          when(it) {
            is CastVoteClick -> {
             val selectedCandidates = it.candidates as ArrayList<String>
              if (selectedCandidates.size == 1) {
                checkAndNavigate {
                  viewModel.castVote(id!!, selectedCandidates[0], passCode ?: "")
                }
              } else {
                var ballotInput = ""
                for (i in 0 until selectedCandidates.size) {
                  ballotInput += if (i == selectedCandidates.size - 1) {
                    selectedCandidates[i]
                  } else {
                    selectedCandidates[i] + ">"
                  }
                }
                checkAndNavigate { viewModel.castVote(id!!, ballotInput, passCode ?: "") }
              }
            }
          }
        }
      }
    }

    initObservers()
    checkAndNavigate {
      intent?.let {
        if (it.data != null) {
          processURL(it.data)
        } else {
          it.getStringExtra(AppConstants.ELECTION_ID)?.let { electionId ->
            id = electionId
            viewModel.verifyVoter(electionId)
          }
        }
      }
    }
  }

  private fun initObservers() {
    lifecycleScope.launch {
      viewModel.getDeepLinkStateFlow.collect {
        it?.let { response ->
          it.status?.let { status ->
            when (status) {
              ResponseUI.Status.LOADING -> {}
              ResponseUI.Status.SUCCESS -> {
                response.message?.let { message ->
                  val strings = message.split("/")
                  passCode = strings[3]
                  id = strings[2]
                  checkAndNavigate {
                    viewModel.verifyVoter(strings[2])
                  }
                }
              }
              ResponseUI.Status.ERROR -> navigateToMainActivity(
                response.message ?: getString(string.invalid_url)
              )
            }
          }
        }
      }
    }
  }

  private fun processURL(encodedURL: Uri?) {
    if (encodedURL != null) {
      viewModel.getResolvedPath(encodedURL.toString())
    } else {
      navigateToMainActivity(getString(string.invalid_url))
    }
  }

  private fun checkAndNavigate(funtion: () -> Unit) {
    if (internetManager.isConnected()) funtion.invoke()
    else navigateToMainActivity(getString(string.no_network))
  }

  private fun navigateToMainActivity(message: String) {
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra(AppConstants.SHOW_SNACKBAR_KEY, message)
    startActivity(intent)
    finish()
  }

  override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    startActivity(intent)
    finish()
  }
}
