package org.aossie.agoraandroid.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_profile.user_name_til
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

  lateinit var binding: FragmentProfileBinding
  lateinit var viewModel: ProfileViewModel
  lateinit var loadToast: LoadToast

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    viewModel = ViewModelProvider(this)
      .get(ProfileViewModel::class.java)
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

    loadToast = LoadToast(activity)
    binding.viewModel = viewModel

    binding.changePasswordBtn.setOnClickListener {
      loadToast.show()
      viewModel.changePassword(
        binding.newPasswordEt.text.toString(),
        binding.confirmPasswordEt.text.toString()
      )
    }

    binding.updateProfileBtn.setOnClickListener {
      loadToast.show()
      val firstName = binding.firstNameEt.text.toString().trim()
      val lastName = binding.lastNameEt.text.toString().trim()
      val email = binding.emailEt.text.toString().trim()
      val usernameText = binding.usernameEt.text.toString().trim()
      binding.firstNameTil.error = null
      binding.lastNameTil.error = null
      binding.userNameTil.error = null
      binding.emailIdTil.error = null
      when {
        firstName.isEmpty() -> binding.firstNameTil.error = "First Name cannot be empty"
        lastName.isEmpty() -> binding.lastNameTil.error = "Last Name cannot be empty"
        email != viewModel.email -> binding.emailIdTil.error = "Email cannot be changed"
        usernameText != viewModel.userName -> user_name_til.error = "Userame cannot be changed"
        else -> viewModel.updateUser(firstName, lastName, email, usernameText)
      }
    }

    viewModel.passwordRequestCode.observe(viewLifecycleOwner, Observer {
      handlePassword(it)
    })
    viewModel.userUpdateResponse.observe(viewLifecycleOwner, Observer {
      handleUser(it)
    })
    return binding.root
  }
  private fun handleUser(response: ProfileViewModel.ResponseResults) = when(response) {
    is ProfileViewModel.ResponseResults.Success -> {
      loadToast.success()
      Toast.makeText(activity, getString(string.user_updated), Toast.LENGTH_SHORT).show()
    }
    is ProfileViewModel.ResponseResults.Error -> {
      loadToast.error()
      Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
    }
  }
  private fun handlePassword(it: Int?) {
    binding.newPasswordTil.error = null
    binding.confirmPasswordTil.error = null
    when (it) {
      1 -> {
        loadToast.error()
        binding.newPasswordTil.error = getString(R.string.password_empty_warn)
      }
      2 -> {
        loadToast.error()
        binding.confirmPasswordTil.error = getString(R.string.password_empty_warn)
      }
      3 -> {
        loadToast.error()
        binding.newPasswordTil.error = getString(R.string.password_not_match_warn)
        binding.confirmPasswordTil.error = getString(R.string.password_not_match_warn)
      }
      4 -> {
        loadToast.error()
        binding.newPasswordTil.error = getString(R.string.password_same_oldpassword_warn)
        binding.confirmPasswordTil.error = getString(R.string.password_same_oldpassword_warn)
      }
      200 -> {
        loadToast.success()
        Toast.makeText(activity, getString(R.string.password_change_success), Toast.LENGTH_SHORT)
          .show()
      }
      201 -> {
        loadToast.error()
        Toast.makeText(activity, getString(R.string.token_expired), Toast.LENGTH_SHORT)
          .show()
      }
      500 -> {
        loadToast.error()
        Toast.makeText(activity, "something wrong! please try later", Toast.LENGTH_SHORT)
          .show()
      }

    }
  }

}