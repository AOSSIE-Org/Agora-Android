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

    binding.firstNameTiet.addTextChangedListener(getTextWatcher(1))
    binding.lastNameTiet.addTextChangedListener(getTextWatcher(2))
    binding.newPasswordTiet.addTextChangedListener(getTextWatcher(3))
    binding.confirmPasswordTiet.addTextChangedListener(getTextWatcher(4))

    binding.updateProfileBtn.setOnClickListener {
      loadToast.show()
      if(binding.firstNameTil.error == null && binding.lastNameTil.error == null)
        viewModel.updateUser(
            binding.firstNameTiet.text.toString().trim(),
            binding.lastNameTiet.text.toString().trim()
        )
      else loadToast.error()
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
            loadToast.show()
            viewModel.changePassword(binding.newPasswordTiet.text.toString())
          }
        }
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
  private fun handlePassword(response: ProfileViewModel.ResponseResults) = when(response) {
    is ProfileViewModel.ResponseResults.Success -> {
      loadToast.success()
      Toast.makeText(activity, getString(string.password_change_success), Toast.LENGTH_SHORT).show()
    }
    is ProfileViewModel.ResponseResults.Error -> {
      loadToast.error()
      Toast.makeText(activity, response.message, Toast.LENGTH_SHORT).show()
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
              s.toString() == viewModel.pass -> binding.newPasswordTil.error = getString(string.password_same_oldpassword_warn)
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
}