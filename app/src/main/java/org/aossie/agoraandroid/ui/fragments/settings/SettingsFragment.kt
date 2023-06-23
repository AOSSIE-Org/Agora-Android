package org.aossie.agoraandroid.ui.fragments.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.string
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
import org.aossie.agoraandroid.ui.screens.settings.SettingsScreenEvent.OnSnackBarActionClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.isUrl
import org.aossie.agoraandroid.utilities.toByteArray
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
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

  private var mAvatar = MutableLiveData<File>()

 private lateinit var composeView: ComposeView

  private lateinit var mUser: UserModel

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
      val appLanguageState by homeViewModel.appLanguage.collectAsState("")
      val supportedLang = homeViewModel.getSupportedLanguages
      val avatar by mAvatar.observeAsState()
      val progressErrorState by homeViewModel.progressAndErrorState.collectAsState()


      AgoraTheme {
        SettingScreen(userState,avatar,appLanguageState,supportedLang,progressErrorState){ event->
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
            OnSnackBarActionClick -> {
              homeViewModel.hideSnackBar()
            }

            is ChangeAppLanguage -> {
              homeViewModel.changeLanguage(event.language,context)
            }
          }
        }
      }
    }

    lifecycleScope.launch {
      val user = viewModel.user
      user.collect {
        if (it != null) {
          mUser = it
          if (it.avatarURL != null) {
            if (!it.avatarURL.isUrl()) {
              val bitmap = decodeBitmap(it.avatarURL)
              setAvatar(bitmap)
            }
          }
        }
      }
    }

    lifecycleScope.launch {
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
  }

  private fun decodeBitmap(encodedBitmap: String): Bitmap {
    val decodedString = Base64.decode(encodedBitmap, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  private fun setAvatar(bitmap: Bitmap) {
    val bytes = bitmap.toByteArray(Bitmap.CompressFormat.PNG)
    try {
      val avatar = File(context?.cacheDir, "avatar")
      if (avatar.exists()) {
        avatar.delete()
      }
      val fos = FileOutputStream(avatar)
      fos.write(bytes)
      fos.flush()
      fos.close()
      mAvatar.value = avatar
    } catch (e: IOException) {
      e.printStackTrace()
      notify(getString(string.error_loading_image))
    }
  }
}
