package org.aossie.agoraandroid.ui.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.screens.welcome.WelcomeScreen
import org.aossie.agoraandroid.ui.screens.welcome.WelcomeScreenEvent.ALREADY_HAVE_ACCOUNT_CLICKED
import org.aossie.agoraandroid.ui.screens.welcome.WelcomeScreenEvent.GET_STARTED_CLICKED
import org.aossie.agoraandroid.ui.theme.AgoraTheme

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

  private lateinit var composeView:ComposeView

  private val data: Array<Pair<Int, Int>> = arrayOf(
    Pair(R.string.first_welcome_message, R.drawable.img_empowering_democracy),
    Pair(R.string.second_welcome_message, R.drawable.img_create_schedule_election),
    Pair(R.string.third_welcome_message, R.drawable.img_invite_voters_elect),
    Pair(R.string.fourth_welcome_message, R.drawable.img_visualize_results)
  )

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    composeView.setContent {
      AgoraTheme {
        Surface(
          modifier = Modifier.fillMaxSize(),
          color = MaterialTheme.colorScheme.background
        ) {
          WelcomeScreen(data){
            when(it){
              ALREADY_HAVE_ACCOUNT_CLICKED -> {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
              }
              GET_STARTED_CLICKED -> {
                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSignUpFragment())
              }
            }
          }
        }
      }
    }
  }
}
