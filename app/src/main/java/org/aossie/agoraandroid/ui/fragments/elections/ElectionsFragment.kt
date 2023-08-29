package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.elections.ElectionsScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

class ElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  private val onItemClicked = { _id: String ->
    val action = ElectionsFragmentDirections
      .actionElectionsFragmentToElectionDetailsFragment(_id)
    findNavController().navigate(action)
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
    bindUI()
    composeView.setContent {
      val elections by electionViewModel.elections.collectAsState()
      val progressErrorState by electionViewModel.progressAndErrorState.collectAsState()
      val searchText by electionViewModel.search
      AgoraTheme {
        ElectionsScreen(
          screenState = progressErrorState,
          elections = elections,
          searchText = searchText,
          onSearch = {
            electionViewModel.getElectionsState(it)
          },
          onItemClicked = onItemClicked
        )
      }
    }
  }

  private fun bindUI() {
    electionViewModel.getElectionsState(electionViewModel.search.value)
  }

  override fun onNetworkConnected() {
    bindUI()
  }
}
