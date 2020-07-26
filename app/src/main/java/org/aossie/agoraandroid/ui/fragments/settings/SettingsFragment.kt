package org.aossie.agoraandroid.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
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
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.shortSnackbar
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
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

  lateinit var binding: FragmentSettingsBinding

  lateinit var mUser: User

  private val viewModel: ProfileViewModel by viewModels {
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
    user.observe(viewLifecycleOwner, Observer {
      binding.user = it
      mUser = it
    })


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
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
  }

}
