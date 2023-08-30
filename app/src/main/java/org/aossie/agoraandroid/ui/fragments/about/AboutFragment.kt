package org.aossie.agoraandroid.ui.fragments.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.ui.screens.about.AboutScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

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

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    composeView.setContent {
      AgoraTheme {
        AboutScreen()
      }
    }
  }
}
