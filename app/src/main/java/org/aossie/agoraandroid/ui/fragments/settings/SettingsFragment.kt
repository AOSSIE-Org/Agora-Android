package org.aossie.agoraandroid.ui.fragments.settings

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat.PNG
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.Factory
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
import com.squareup.picasso.NetworkPolicy.OFFLINE
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.FragmentSettingsBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.ResponseUI.Status.ERROR
import org.aossie.agoraandroid.utilities.ResponseUI.Status.LOADING
import org.aossie.agoraandroid.utilities.ResponseUI.Status.SUCCESS
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.isUrl
import org.aossie.agoraandroid.utilities.loadImage
import org.aossie.agoraandroid.utilities.loadImageFromMemoryNoCache
import org.aossie.agoraandroid.utilities.show
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
  private val viewModelFactory: Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private var mAvatar = MutableLiveData<File>()

  private lateinit var binding: FragmentSettingsBinding

  private lateinit var mUser: User

  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSettingsBinding.inflate(inflater)

    return binding.root
  }
  override fun onFragmentInitiated() {
    val user = viewModel.user
    user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          binding.tvEmailId.text = it.email
          binding.tvName.text = (it.firstName ?: "") + " " + (it.lastName ?: "")
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
        binding.imageView.loadImageFromMemoryNoCache(it)
      }
    )

    homeViewModel.getLogoutLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ERROR -> {
            binding.progressBar.hide()
            notify(it.message)
            binding.tvLogout.toggleIsEnable()
          }
          SUCCESS -> {
            binding.progressBar.hide()
            lifecycleScope.launch {
              if (prefs.getIsFacebookUser().first()) {
                LoginManager.getInstance()
                  .logOut()
              }
            }
            homeViewModel.deleteUserData()
            notify("Logged Out")
            Navigation.findNavController(binding.root)
              .navigate(
                SettingsFragmentDirections.actionSettingsFragmentToWelcomeFragment()
              )
          }
          LOADING -> {
            binding.progressBar.show()
            binding.tvLogout.toggleIsEnable()
          }
        }
      }
    )
    homeViewModel.sessionExpiredListener = this

    binding.tvAccountSettings.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
    }

    binding.tvShare.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToShareWithOthersFragment()
        )
    }

    binding.tvAbout.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToAboutFragment()
        )
    }

    binding.tvContactUs.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(
          SettingsFragmentDirections.actionSettingsFragmentToContactUsFragment()
        )
    }

    binding.tvLogout.setOnClickListener {
      homeViewModel.doLogout()
    }
  }

  private fun cacheAndSaveImage(url: String) {
    binding.imageView.loadImage(url, OFFLINE) {
      binding.imageView.loadImage(url)
    }
  }

  private fun decodeBitmap(encodedBitmap: String): Bitmap {
    val decodedString = Base64.decode(encodedBitmap, Base64.NO_WRAP)
    return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
  }

  private fun setAvatar(bitmap: Bitmap) {
    val bytes = bitmap.toByteArray(PNG)
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
