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
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
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
        binding.newPasswordTil.editText!!.text.toString(),
        binding.confirmPasswordTil.editText!!.text.toString()
      )
    }

    binding.updateProfileBtn.setOnClickListener {
      loadToast.show()
      viewModel.updateUser(
        binding.firstNameTil.editText!!.text.toString().trim(),
        binding.lastNameTil.editText!!.text.toString().trim(),
        binding.emailIdTil.editText!!.text.toString().trim(),
        binding.userNameTil.editText!!.text.toString().trim()
      )
    }

    viewModel.passwordRequestCode.observe(viewLifecycleOwner, Observer {
      handlePassword(it)
    })

    viewModel.updateUserRequestCode.observe(viewLifecycleOwner, Observer {
      handleUser(it)
    })
    return binding.root
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

  private fun handleUser(i: Int?){
    binding.firstNameTil.error = null
    binding.lastNameTil.error = null
    binding.userNameTil.error = null
    binding.emailIdTil.error = null

    when(i){
      1 -> {
        loadToast.error()
        binding.firstNameTil.error = "First name cannot be empty"
      }
      2 -> {
        loadToast.error()
        binding.lastNameTil.error = "Last name cannot be empty"
      }
      3 -> {
        loadToast.error()
        binding.emailIdTil.error = "Email cannot be changed"
      }
      4 -> {
        loadToast.error()
        binding.userNameTil.error = "Username cannot be changed"
      }
      200 -> {
        loadToast.success()
        Toast.makeText(activity, "User updated successfully!", Toast.LENGTH_SHORT).show()
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