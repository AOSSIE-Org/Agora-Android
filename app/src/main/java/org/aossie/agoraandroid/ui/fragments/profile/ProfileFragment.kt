package org.aossie.agoraandroid.ui.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel.UiEvents.UserLoggedOut
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.UiEvents.PasswordChanged
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.UiEvents.TwoFactorAuthToggled
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.profile.ProfileScreen
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

class ProfileFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }
  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }
  private val homeViewModel: HomeViewModel by viewModels {
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
    homeViewModel.sessionExpiredListener = this
    loginViewModel.sessionExpiredListener = this
    viewModel.sessionExpiredListener = this

    composeView.setContent {

      val progressErrorState1 = viewModel.progressAndErrorState
      val progressErrorState2 = homeViewModel.progressAndErrorState

      val progressErrorState by merge(progressErrorState1,progressErrorState2).collectAsState(initial = ScreensState())

      val userData by viewModel.userModelState.collectAsState()
      val profileDataState by viewModel.profileDataState.collectAsState()

      LaunchedEffect(key1 = viewModel) {
        viewModel.uiEvents.collectLatest {
          when(it) {
            PasswordChanged -> {
              loginViewModel.logInRequest(
                userData?.username!!, profileDataState.confirmPassword, userData.trustedDevice
              )
            }
            TwoFactorAuthToggled -> {
              homeViewModel.doLogout()
            }
          }
        }
      }
      LaunchedEffect(key1 = viewModel) {
        homeViewModel.uiEvents.collectLatest {
          when(it) {
            UserLoggedOut -> {
              lifecycleScope.launch {
                if (prefs.getIsFacebookUser().first()) {
                  LoginManager.getInstance()
                    .logOut()
                }
              }
              homeViewModel.deleteUserData()
              delay(2000)
              findNavController().navigate(
                  ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
                )
            }
          }
        }
      }
      AgoraTheme {
        ProfileScreen(
          prefs = prefs,
          screenState = progressErrorState,
          userData = userData,
          profileDataState = profileDataState,
        ){
          viewModel.onEvent(it)
        }
      }
    }
  }
}