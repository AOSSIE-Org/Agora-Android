package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.ballotslist.BallotsListScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null
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
    electionDetailsViewModel.sessionExpiredListener = this
    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getBallot(id)
    composeView.setContent {
      val progressErrorState by electionDetailsViewModel.progressAndErrorState
      val ballotsList by electionDetailsViewModel.ballotsListState.collectAsState()
      AgoraTheme() {
        BallotsListScreen(
          ballotsList = ballotsList,
          progressErrorState = progressErrorState)
      }
    }
  }

  override fun onNetworkConnected() {
    electionDetailsViewModel.getBallot(id)
  }
}
