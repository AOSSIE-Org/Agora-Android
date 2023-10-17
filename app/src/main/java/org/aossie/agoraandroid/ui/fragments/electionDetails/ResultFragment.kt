package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.electionDetails.ElectionDetailsViewModel.UiEvents.ShareFile
import org.aossie.agoraandroid.ui.screens.result.ResultScreen
import org.aossie.agoraandroid.ui.screens.result.ResultScreenEvent.OnBackClick
import org.aossie.agoraandroid.ui.screens.result.ResultScreenEvent.OnExportCSVClick
import org.aossie.agoraandroid.ui.screens.result.ResultScreenEvent.OnShareClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ResultFragment
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

  private fun setContent() {
    composeView.setContent {

      val progressErrorState by electionDetailsViewModel.progressAndErrorState
      val winnerDtoModel by electionDetailsViewModel.resultState

      LaunchedEffect(key1 = electionDetailsViewModel) {
        electionDetailsViewModel.uiEventsFlow.collectLatest { event->
          when(event) {
            is ShareFile -> {
              lifecycleScope.launch {
                val shareIntent = Intent()
                shareIntent.let {
                  it.action = Intent.ACTION_SEND
                  it.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                  it.setDataAndType(event.uri, requireContext().contentResolver.getType(event.uri))
                  it.putExtra(Intent.EXTRA_STREAM, event.uri)
                }
                startActivity(Intent.createChooser(shareIntent, getString(string.share_result)))
              }
            }
            else -> {}
          }
        }
      }

      AgoraTheme() {
        ResultScreen(
          progressErrorState = progressErrorState,
          winnerDto = winnerDtoModel
        ) { event->
          when(event) {
            OnBackClick -> {
              findNavController().popBackStack()
            }
            OnExportCSVClick -> {
              id?.let {
                electionDetailsViewModel.createExcelFile(requireContext(), it)
              }
            }
            is OnShareClick -> {
              electionDetailsViewModel.createImage(requireContext(), event.bitmap)
            }
          }
        }
      }
    }
  }

  override fun onFragmentInitiated() {
    electionDetailsViewModel.sessionExpiredListener = this

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getResult(id)
    setContent()
  }
}
