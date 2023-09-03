package org.aossie.agoraandroid.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.model.UserModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel.UiEvents.UserLoggedOut
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel
import org.aossie.agoraandroid.ui.screens.settings.SettingScreen
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.ChangeAppLanguage
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnAboutUsClick
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnAccountSettingClick
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnContactUsClick
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnLogoutClick
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnShareWithOthersClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }
  private lateinit var composeView: ComposeView

  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }

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

    composeView.setContent {

      val context = LocalContext.current
      val userState by viewModel.user.collectAsState(UserModel())
      val profileDataState by viewModel.profileDataState.collectAsState()
      val appLanguageState by homeViewModel.appLanguage.collectAsState("")
      val supportedLang = homeViewModel.getSupportedLanguages
      val progressErrorState by homeViewModel.progressAndErrorState.collectAsState()

      LaunchedEffect(key1 = Unit) {
        homeViewModel.uiEvents.collectLatest { event ->
          when(event){
            UserLoggedOut -> {
              lifecycleScope.launch {
                if (prefs.getIsFacebookUser().first()) {
                  LoginManager.getInstance()
                    .logOut()
                }
              }
              homeViewModel.deleteUserData()
              notify("Logged Out")
              findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToWelcomeFragment()
              )
            }
          }
        }
      }

      AgoraTheme {
        SettingScreen(userState,profileDataState.avatar,appLanguageState,supportedLang,progressErrorState){ event->
          when(event){
            OnAboutUsClick -> {
              findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToAboutFragment()
              )
            }
            OnAccountSettingClick -> {
              findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToProfileFragment()
              )
            }
            OnContactUsClick -> {
              findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToContactUsFragment()
              )
            }
            OnLogoutClick -> {
              homeViewModel.doLogout()
            }
            OnShareWithOthersClick -> {
              findNavController().navigate(
                SettingsFragmentDirections.actionSettingsFragmentToShareWithOthersFragment()
              )
            }
            is ChangeAppLanguage -> {
              homeViewModel.changeLanguage(event.language,context)
            }
          }
        }
      }
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.loadData()
  }
}
