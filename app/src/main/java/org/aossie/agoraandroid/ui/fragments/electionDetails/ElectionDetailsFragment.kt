package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsViewModel.UiEvents.ElectionDeleted
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsViewModel.UiEvents.InviteVoters
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsViewModel.UiEvents.ViewResults
import org.aossie.agoraandroid.ui.screens.electionDetails.ElectionDetailsScreen
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.BallotClick
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.OnSnackActionClick
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent.ViewVotersClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */

class ElectionDetailsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private var id: String? = null
  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }
  private lateinit var composeView: ComposeView
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  override fun onFragmentInitiated() {
    val args =
      ElectionDetailsFragmentArgs.fromBundle(
        requireArguments()
      )
    id = args.id
    electionDetailsViewModel.sessionExpiredListener = this
    setObserver()
    electionDetailsViewModel.getElectionDetailsById(id ?: "")

    composeView.setContent {
      val progressErrorState by electionDetailsViewModel.progressAndErrorState
      val electionDetails by electionDetailsViewModel.electionState
      AgoraTheme {
        ElectionDetailsScreen(
          screenState =  progressErrorState,
          electionDetails = electionDetails
        ) { event->
          when(event){
            OnSnackActionClick -> {
              electionDetailsViewModel.hideSnackBar()
            }
            BallotClick -> {
              val action =
                ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToBallotFragment(
                  id!!
                )
              findNavController().navigate(action)
            }
            ViewVotersClick -> {
              val action =
                ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToVotersFragment(
                  id!!
                )
              findNavController().navigate(action)
            }
            else -> electionDetailsViewModel.onEvent(event)
          }
        }
      }
    }
  }

  override fun onNetworkConnected() {
    electionDetailsViewModel.getElectionDetailsById(id ?: "")
  }

  private fun setObserver() = lifecycleScope.launch {
    viewLifecycleOwner.repeatOnLifecycle(state = State.STARTED) {
      electionDetailsViewModel.uiEventsFlow.collectLatest {
        when(it){
          ElectionDeleted -> {
            lifecycleScope.launch {
              prefs.setUpdateNeeded(true)
            }
            findNavController().navigate(
                ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToHomeFragment()
              )
          }
          InviteVoters -> {
            val action =
              ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToInviteVotersFragment(
                id!!
              )
            findNavController().navigate(action)
          }
          ViewResults -> {
            if (isConnected) {
              val action =
                ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToResultFragment(
                  id!!
                )
              findNavController().navigate(action)
            } else {
              electionDetailsViewModel.showMessageResource(R.string.no_network)
            }
          }
          else -> {}
        }
      }
    }
  }
}
