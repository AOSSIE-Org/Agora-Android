package org.aossie.agoraandroid.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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

    viewModel = ViewModelProviders.of(this)
        .get(ProfileViewModel::class.java)
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

    loadToast = LoadToast(activity)
    binding.viewModel = viewModel

    binding.changePasswordBtn.setOnClickListener {
      loadToast.show()
      viewModel.changePassword(
          binding.currentPasswordTil.editText!!.text.toString(),
          binding.newPasswordTil.editText!!.text.toString(),
          binding.confirmPasswordTil.editText!!.text.toString()
      )
    }

    binding.updateProfileBtn.setOnClickListener({
      //TODO implment update feature
      Toast.makeText(activity, "feature not available yet", Toast.LENGTH_SHORT)
          .show()
    })

    viewModel.passwordRequestCode.observe(this, Observer {
      handlePassword(it)
    })
    return binding.getRoot()
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
      5 -> {
        loadToast.error()
        binding.currentPasswordTil.error = getString(R.string.current_password_empty_warn)
      }
      6 -> {
        loadToast.error()
        binding.currentPasswordTil.error = getString(R.string.current_password_match_warn)
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