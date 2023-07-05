package org.aossie.agoraandroid.ui.fragments.createelection

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.createelection.CreateElectionViewModel.CreateElectionUiEvents.ElectionCreated
import org.aossie.agoraandroid.ui.screens.createElection.CreateElectionsScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

const val STORAGE_PERMISSION_REQUEST_CODE = 2
const val STORAGE_INTENT_REQUEST_CODE = 4

/**
 * A simple [Fragment] subclass.
 */
class CreateElectionFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val createElectionViewModel: CreateElectionViewModel by viewModels {
    viewModelFactory
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

  @RequiresApi(VERSION_CODES.O)
  override fun onFragmentInitiated() {
    composeView.setContent {

      val electionDataState by createElectionViewModel.createElectionDataState.collectAsState()
      val progressErrorState by createElectionViewModel.progressAndErrorState.collectAsState()

      LaunchedEffect(key1 = true) {
        createElectionViewModel.uiEvents.collectLatest {
          when(it) {
            ElectionCreated -> {
              findNavController()
                .navigate(CreateElectionFragmentDirections.actionCreateElectionFragmentToHomeFragment())
            }
          }
        }
      }

      AgoraTheme {
        CreateElectionsScreen(
          electionDataState = electionDataState,
          progressErrorState = progressErrorState,
        ){event ->
          when(event){
            else -> {
              createElectionViewModel.onEvent(event)
            }
          }
        }
      }
    }
  }
}