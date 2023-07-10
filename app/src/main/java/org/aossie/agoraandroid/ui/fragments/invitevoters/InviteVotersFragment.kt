package org.aossie.agoraandroid.ui.fragments.invitevoters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.invitevoters.InviteVotersViewModel.InviteVotersUiEvents.VotersInvited
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreen
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.InviteVotersClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class InviteVotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private var id: String? = null
  private val inviteVotersViewModel: InviteVotersViewModel by viewModels {
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
    inviteVotersViewModel.sessionExpiredListener = this
    id = InviteVotersFragmentArgs.fromBundle(requireArguments()).id
    composeView.setContent {

      val progressErrorState by inviteVotersViewModel.progressAndErrorState
      val dataState by inviteVotersViewModel.inviteVotersDataState.collectAsState()
      val votersListState by inviteVotersViewModel.votersListState.collectAsState()

      LaunchedEffect(key1 = inviteVotersViewModel){
        inviteVotersViewModel.uiEvents.collectLatest {
          when(it) {
            VotersInvited -> {
              id?.let {
                for (voter in votersListState) {
                  inviteVotersViewModel.sendFCM(
                    voter.voterEmail,
                    getString(R.string.fcm_title),
                    getString(R.string.fcm_body),
                    it
                  )
                }
              }
              prefs.setUpdateNeeded(true)
              findNavController().popBackStack()
            }
          }
        }
      }

      AgoraTheme {
        InviteVotersScreen(
          progressErrorState = progressErrorState,
          voterDataState = dataState,
          votersListState = votersListState
        ){
          when(it) {
            InviteVotersClick -> {
              inviteVotersViewModel.inviteVoters(votersListState,id!!)
            }
            else -> {
              inviteVotersViewModel.onEvent(it)
            }
          }
        }
      }
    }
  }
}
