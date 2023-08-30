package org.aossie.agoraandroid.ui.fragments.displayelections

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
import androidx.navigation.fragment.findNavController
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.elections.ElectionsScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ActiveElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val displayElectionViewModel: DisplayElectionViewModel by viewModels {
    viewModelFactory
  }
  private val onItemClicked = { _id: String ->
    val action =
      ActiveElectionsFragmentDirections.actionActiveElectionsFragmentToElectionDetailsFragment(_id)
    findNavController().navigate(action)
  }
  private lateinit var composeView: ComposeView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  override fun onFragmentInitiated() {
    bindUI()
    composeView.setContent {
      val elections by displayElectionViewModel.activeElections.collectAsState()
      val progressErrorState by displayElectionViewModel.progressAndErrorState.collectAsState()
      val searchText by displayElectionViewModel.search
      AgoraTheme {
        ElectionsScreen(
          screenState = progressErrorState,
          elections = elections,
          searchText = searchText,
          onSearch = {
            displayElectionViewModel.getActiveElectionsState(it)
          },
          onItemClicked = onItemClicked
        )
      }
    }

  }

  override fun onNetworkConnected() {
    bindUI()
  }

  private fun bindUI() {
    displayElectionViewModel.getActiveElectionsState("")
  }
}
