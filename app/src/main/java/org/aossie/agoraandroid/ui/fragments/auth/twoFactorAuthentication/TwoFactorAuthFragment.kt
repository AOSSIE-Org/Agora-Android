package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collectLatest
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication.TwoFactorAuthViewModel.UiEvents.TwoFactorAuthComplete
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreen
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.OnBackClick
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.ResendOtpClick
import org.aossie.agoraandroid.ui.screens.auth.twoFactorAuth.TwoFactorAuthScreenEvent.VerifyOtpClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.navigateSafely
import javax.inject.Inject

class TwoFactorAuthFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private var crypto: String? = null

  private val viewModel: TwoFactorAuthViewModel by viewModels {
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
    crypto = TwoFactorAuthFragmentArgs.fromBundle(requireArguments()).crypto
    viewModel.sessionExpiredListener = this

    composeView.setContent {
      val progressErrorState by viewModel.progressAndErrorState
      LaunchedEffect(key1 = viewModel) {
        viewModel.uiEventsFlow.collectLatest {
          when(it) {
            TwoFactorAuthComplete -> {
              findNavController()
                .navigateSafely(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
            }
          }
        }
      }
      AgoraTheme {
        TwoFactorAuthScreen(
          progressErrorState = progressErrorState
        ) {event ->
          when(event) {
            ResendOtpClick -> viewModel.resendOTP()
            is VerifyOtpClick -> viewModel.verifyOTP(event.otp, event.trustedDevice)
            OnBackClick -> findNavController().navigateUp()
          }
        }
      }
    }
  }
}