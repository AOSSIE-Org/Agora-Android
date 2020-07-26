package org.aossie.agoraandroid.ui.fragments.profile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.login.LoginManager
import kotlinx.android.synthetic.main.fragment_profile.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.FragmentProfileBinding
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.home.HomeViewModel
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Error
import org.aossie.agoraandroid.ui.fragments.profile.ProfileViewModel.ResponseResults.Success
import org.aossie.agoraandroid.ui.fragments.settings.SettingsFragmentDirections
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInFrag
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.shortSnackbar
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class ProfileFragment
  @Inject
  constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
      private val prefs: PreferenceProvider
  ): Fragment() , AuthListener {

  lateinit var binding: FragmentProfileBinding
  private val viewModel: ProfileViewModel by viewModels {
    viewModelFactory
  }
  private val loginViewModel: LoginViewModel by viewModels{
    viewModelFactory
  }

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mUser: User

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    homeViewModel.authListener = this

    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
    viewModel.user.observe(viewLifecycleOwner, Observer {
      if(it != null) {
        Log.d("friday", it.toString())
        binding.user = it
        mUser = it
      }
    })
    binding.firstNameTiet.addTextChangedListener(getTextWatcher(1))
    binding.lastNameTiet.addTextChangedListener(getTextWatcher(2))
    binding.newPasswordTiet.addTextChangedListener(getTextWatcher(3))
    binding.confirmPasswordTiet.addTextChangedListener(getTextWatcher(4))

    binding.updateProfileBtn.setOnClickListener {
      binding.root.progress_bar.show()
      if(binding.firstNameTil.error == null && binding.lastNameTil.error == null) {
      hideKeyboardInFrag(this@ProfileFragment)
          viewModel.updateUser(
              mUser
          )
      }
      else binding.root.progress_bar.hide()

    }

    binding.switchWidget.setOnClickListener {
      if(binding.switchWidget.isChecked) {
        AlertDialog.Builder(context!!)
            .setTitle("Please Confirm")
            .setMessage("Are you sure you want to enable two factor authentication")
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { dialog, _ ->
              binding.root.progress_bar.show()
              viewModel.toggleTwoFactorAuth()
              dialog.cancel()
            }
            .setNegativeButton(android.R.string.cancel){ dialog, _ ->
              dialog.cancel()
            }
            .create()
            .show()
      }
      else{
          AlertDialog.Builder(context!!)
              .setTitle("Please Confirm")
              .setMessage("Are you sure you want to disable two factor authentication")
              .setCancelable(false)
              .setPositiveButton(android.R.string.ok) { dialog, _ ->
                binding.root.progress_bar.show()
                viewModel.toggleTwoFactorAuth()
                dialog.cancel()
              }
              .setNegativeButton(android.R.string.cancel){ dialog, _ ->
                dialog.cancel()
              }
              .create()
              .show()
        }
    }


    binding.changePasswordBtn.setOnClickListener {
      val newPass = binding.newPasswordTiet.text.toString()
      val conPass = binding.confirmPasswordTiet.text.toString()
      if(binding.newPasswordTil.error == null && binding.confirmPasswordTil.error == null) {
        when {
          newPass.isEmpty() -> binding.newPasswordTil.error = getString(string.password_empty_warn)
          conPass.isEmpty() -> binding.confirmPasswordTil.error = getString(string.password_empty_warn)
          newPass != conPass -> binding.confirmPasswordTil.error = getString(string.password_not_match_warn)
          else -> {
            binding.root.progress_bar.show()
            hideKeyboardInFrag(this@ProfileFragment)
            viewModel.changePassword(binding.newPasswordTiet.text.toString())
          }
        }
      }
      else binding.root.progress_bar.hide()
    }

    viewModel.passwordRequestCode.observe(viewLifecycleOwner, Observer {
      handlePassword(it)
    })
    viewModel.userUpdateResponse.observe(viewLifecycleOwner, Observer {
      handleUser(it)
    })

    viewModel.toggleTwoFactorAuthResponse.observe(viewLifecycleOwner, Observer {
      handleTwoFactorAuthentication(it)
      homeViewModel.doLogout()
    })
    return binding.root
  }
  private fun handleUser(response: ResponseResults) = when(response) {
    is Success -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message.toString())
      loginViewModel.logInRequest(mUser.username!!, mUser.password!!, mUser.trustedDevice)
    }
    is Error -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message)
    }
  }
  private fun handleTwoFactorAuthentication(response: ResponseResults) = when(response) {
    is Success -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message.toString() + ", Please login again")
    }
    is Error -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message)
    }
  }
  private fun handlePassword(response: ResponseResults) = when(response) {
    is Success -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message.toString())
      loginViewModel.logInRequest(mUser.username!!, binding.newPasswordTiet.text.toString(), mUser.trustedDevice)
    }
    is Error -> {
      binding.root.progress_bar.hide()
      binding.root.snackbar(response.message)
    }
  }

  private fun getTextWatcher(code : Int): TextWatcher {
    return object : TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        when(code){
          1 -> {
            if(s.isNullOrEmpty()) binding.firstNameTil.error = getString(string.first_name_empty)
            else binding.firstNameTil.error = null
          }
          2 -> {
            if(s.isNullOrEmpty()) binding.lastNameTil.error = getString(string.last_name_empty)
            else binding.lastNameTil.error = null
          }
          3 -> {
            when {
              s.isNullOrEmpty() -> binding.newPasswordTil.error = getString(string.password_empty_warn)
              s.toString() == mUser.password -> binding.newPasswordTil.error = getString(string.password_same_oldpassword_warn)
              else -> binding.newPasswordTil.error = null
            }
          }
          4 -> {
            when {
              s.isNullOrEmpty() -> binding.confirmPasswordTil.error = getString(string.password_empty_warn)
              s.toString() != binding.newPasswordTiet.text.toString() -> binding.confirmPasswordTil.error = getString(string.password_not_match_warn)
              else -> binding.confirmPasswordTil.error = null
            }
          }
        }
      }
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
  }

  override fun onSuccess(message: String?) {
    binding.root.progress_bar.hide()
    if (prefs.getIsFacebookUser()) {
      LoginManager.getInstance()
          .logOut()
    }
    homeViewModel.deleteUserData()
    Navigation.findNavController(binding.root)
        .navigate(
            ProfileFragmentDirections.actionProfileFragmentToWelcomeFragment()
        )
  }

  override fun onStarted() {
    binding.root.progress_bar.show()
  }

  override fun onFailure(message: String) {
    binding.root.progress_bar.hide()
    binding.root.snackbar(message)
  }
}