package org.aossie.agoraandroid.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    binding.firstNameEt.addTextChangedListener(getTextWatcher(1))
    binding.lastNameEt.addTextChangedListener(getTextWatcher(2))
    binding.emailEt.addTextChangedListener(getTextWatcher(3))
    binding.usernameEt.addTextChangedListener(getTextWatcher(4))

    binding.updateProfileBtn.setOnClickListener {
      loadToast.show()
      if(binding.firstNameTil.error == null && binding.lastNameTil.error == null && binding.emailIdTil.error == null && binding.userNameTil.error == null)
        viewModel.updateUser(
            binding.firstNameEt.text.toString().trim(),
            binding.lastNameEt.text.toString().trim(),
            binding.emailEt.text.toString().trim(),
            binding.usernameEt.text.toString().trim()
        )
      else loadToast.error()
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
        Toast.makeText(activity, getString(string.something_went_wrong_please_try_again_later), Toast.LENGTH_SHORT)
          .show()
      }

    }
  }

  private fun getTextWatcher(code : Int): TextWatcher {
    return object : TextWatcher{
      override fun afterTextChanged(s: Editable?) {
        when(code){
          1 -> {
            if(s.isNullOrEmpty()) binding.firstNameTil.error = getString(string.first_name_empty)

          }
          2 -> {
            if(s.isNullOrEmpty()) binding.lastNameTil.error = getString(string.last_name_empty)
            else binding.lastNameTil.error = null
          }
          3 -> {
            if(s.toString() != viewModel.email) binding.emailIdTil.error = getString(string.email_changed)
            else binding.emailIdTil.error = null
          }
          4 -> {
            if(s.toString() != viewModel.userName) binding.userNameTil.error = getString(string.username_changed)
            else binding.userNameTil.error = null
          }
        }
      }
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
  }
}