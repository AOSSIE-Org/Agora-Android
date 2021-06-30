package org.aossie.agoraandroid.ui.fragments.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
import com.squareup.picasso.NetworkPolicy.OFFLINE
import kotlinx.android.synthetic.main.fragment_settings.view.image_view
import kotlinx.android.synthetic.main.fragment_settings.view.progress_bar
import kotlinx.android.synthetic.main.fragment_settings.view.tv_about
import kotlinx.android.synthetic.main.fragment_settings.view.tv_account_settings
import kotlinx.android.synthetic.main.fragment_settings.view.tv_contact_us
import kotlinx.android.synthetic.main.fragment_settings.view.tv_logout
import kotlinx.android.synthetic.main.fragment_settings.view.tv_share
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.FragmentSettingsBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.isUrl
import org.aossie.agoraandroid.utilities.loadImage
import org.aossie.agoraandroid.utilities.loadImageFromMemoryNoCache
import org.aossie.agoraandroid.utilities.shortSnackbar
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toByteArray
import org.aossie.agoraandroid.utilities.toggleIsEnable
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
) : Fragment(), AuthListener {

  private lateinit var rootView: View
  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private var mAvatar = MutableLiveData<File>()

  private lateinit var binding: FragmentSettingsBinding

  private lateinit var mUser: User

  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
    rootView = binding.root

    val user = viewModel.user
    user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          binding.user = it
          mUser = it
          if (it.avatarURL != null) {
            if (it.avatarURL.isUrl())
              cacheAndSaveImage(it.avatarURL)
            else {
              val bitmap = decodeBitmap(it.avatarURL)
              setAvatar(bitmap)
            }
          }
        }
      }
    )

    mAvatar.observe(
      viewLifecycleOwner,
      Observer {
        rootView.image_view.loadImageFromMemoryNoCache(it)
      }
    )

    homeViewModel.authListener = this

    rootView.tv_account_settings.setOnClickListener {
      Navigation.findNavController(rootView)
        .navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
    }

    rootView.tv_share.setOnClickListener {
      Navigation.findNavController(rootView)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToShareWithOthersFragment()
        )
    }

    rootView.tv_about.setOnClickListener {
      Navigation.findNavController(rootView)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToAboutFragment()
        )
    }

    rootView.tv_contact_us.setOnClickListener {
      Navigation.findNavController(rootView)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToContactUsFragment()
        )
    }

    rootView.tv_logout.setOnClickListener {
      homeViewModel.doLogout()
    }

    return rootView
  }

  private fun cacheAndSaveImage(url: String) {
    rootView.image_view.loadImage(url, OFFLINE) {
      rootView.image_view.loadImage(url)
    }
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    if (prefs.getIsFacebookUser()) {
      LoginManager.getInstance()
        .logOut()
    }
    homeViewModel.deleteUserData()
    rootView.shortSnackbar("Logged Out")
    Navigation.findNavController(rootView)
      .navigate(
        SettingsFragmentDirections.actionSettingsFragmentToWelcomeFragment()
      )
  }

  override fun onStarted() {
    rootView.progress_bar.show()
    rootView.tv_logout.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
    rootView.tv_logout.toggleIsEnable()
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
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
      rootView.snackbar("Error while loading the image")
    }
  }
}
