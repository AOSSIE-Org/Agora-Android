package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.FragmentTwoFactorAuthBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.common.utilities.HideKeyboard
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.hide
import org.aossie.agoraandroid.common.utilities.show
import javax.inject.Inject

class TwoFactorAuthFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private var crypto: String? = null
  private var user: User? = null

  private val viewModel: TwoFactorAuthViewModel by viewModels {
    viewModelFactory
  }
  private lateinit var binding: FragmentTwoFactorAuthBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentTwoFactorAuthBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {
    crypto = TwoFactorAuthFragmentArgs.fromBundle(requireArguments()).crypto
    viewModel.sessionExpiredListener = this

    viewModel.user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          user = it
        }
      }
    )

    binding.btnVerifyOtp.setOnClickListener {
      binding.progressBar.show()
      val otp = binding.otpTil.editText
        ?.text
        .toString()
        .trim { it <= ' ' }
      if (otp.isEmpty()) {
        notify(getString(string.enter_otp))
        binding.progressBar.hide()
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        if (binding.cbTrustedDevice.isChecked) {
          viewModel.verifyOTP(
            otp, binding.cbTrustedDevice.isChecked, user!!.crypto!!
          )
        } else {
          binding.progressBar.hide()
          notify(getString(string.tap_on_checkbox))
        }
      }
    }

    binding.tvResendOtp.setOnClickListener {
      if (user != null) {
        binding.progressBar.show()
        viewModel.resendOTP(user!!.username!!)
      } else {
        notify(getString(string.something_went_wrong_please_try_again_later))
      }
    }

    viewModel.verifyOtpResponse.observe(
      viewLifecycleOwner,
      Observer {
        handleVerifyOtp(it)
      }
    )
    viewModel.resendOtpResponse.observe(
      viewLifecycleOwner,
      Observer {
        handleResendOtp(it)
      }
    )
  }
  private fun handleVerifyOtp(response: ResponseUI<Any>) = when (response.status) {
    ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      Navigation.findNavController(binding.root)
        .navigate(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
    }
    ResponseUI.Status.ERROR -> {
      binding.progressBar.hide()
      notify(response.message)
    }

    else -> { // Do Nothing
    }
  }

  private fun handleResendOtp(response: ResponseUI<Any>) = when (response.status) {
    ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      notify(getString(string.otp_sent))
    }
    ResponseUI.Status.ERROR -> {
      binding.progressBar.hide()
      notify(response.message)
    }
    else -> { // Do Nothing
    }
  }
}
