package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreen
import org.aossie.agoraandroid.ui.screens.auth.forgotPassword.ForgotPasswordScreenEvents.OnBackIconClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
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

  override fun onFragmentInitiated() {

    composeView.setContent {

      val userNameState by forgotPasswordViewModel.userNameState.collectAsState()
      val progressErrorState by forgotPasswordViewModel.progressAndErrorState.collectAsState()

      val systemUiController = rememberSystemUiController()
      val useDarkIcons = !isSystemInDarkTheme()

      DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setStatusBarColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        onDispose {}
      }
      AgoraTheme {
        ForgotPasswordScreen(progressErrorState,userNameState) { event ->
          when(event){
            OnBackIconClick -> {
              findNavController().navigateUp()
            }
            else -> {
              forgotPasswordViewModel.onEvent(event)
            }
          }
        }
      }
    }
  }
}
