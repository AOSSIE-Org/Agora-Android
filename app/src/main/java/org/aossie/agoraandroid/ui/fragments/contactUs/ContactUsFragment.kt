package org.aossie.agoraandroid.ui.fragments.contactUs

import android.content.ActivityNotFoundException
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.flow.MutableStateFlow
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.screens.contactUs.ContactUsScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.browse

/**
 * A simple [Fragment] subclass.
 */
class ContactUsFragment : Fragment() {

  private lateinit var composeView: ComposeView
  private val messageState = MutableStateFlow ("")

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    composeView.setContent {
      val message = messageState.collectAsState()
      AgoraTheme {
        ContactUsScreen(
          message,
          onBtnGitlabClicked = {
            openUrl("https://gitlab.com/aossie")
          },
          onBtnGitterClicked = {
            openUrl("https://gitter.im/aossie/home")
          },
          onBtnReportBugClicked = {
            openUrl("https://gitlab.com/aossie/agora-android/issues/new")
          }
        ) {
          messageState.value = ""
        }
      }
    }
  }

  private fun openUrl(url: String) {
    try {
      context?.browse(url)
    } catch (e: ActivityNotFoundException) {
      messageState.value = resources.getString(string.no_browser)
    }
  }
}
