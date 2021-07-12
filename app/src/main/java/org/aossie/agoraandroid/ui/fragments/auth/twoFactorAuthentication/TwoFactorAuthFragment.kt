package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.databinding.FragmentTwoFactorAuthBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class TwoFactorAuthFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(), SessionExpiredListener {

  private lateinit var binding: FragmentTwoFactorAuthBinding

  private var crypto: String? = null
  private var user: User? = null

  private val viewModel: TwoFactorAuthViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentTwoFactorAuthBinding.inflate(layoutInflater)

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
        binding.root.snackbar(getString(string.enter_otp))
        binding.progressBar.hide()
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        if (binding.cbTrustedDevice.isChecked) {
          viewModel.verifyOTP(
            otp, binding.cbTrustedDevice.isChecked, user!!.crypto!!
          )
        } else {
          binding.progressBar.hide()
          binding.root.snackbar(getString(string.tap_on_checkbox))
        }
      }
    }

    binding.tvResendOtp.setOnClickListener {
      if (user != null) {
        binding.progressBar.show()
        viewModel.resendOTP(user!!.username!!)
      } else {
        binding.root.snackbar(getString(string.something_went_wrong_please_try_again_later))
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

    return binding.root
  }

  private fun handleVerifyOtp(response: ResponseUI<Any>) = when (response.status) {
    ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      Navigation.findNavController(binding.root)
        .navigate(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
    }
    ResponseUI.Status.ERROR -> {
      binding.progressBar.hide()
      binding.root.snackbar(response.message)
    }

    else -> { // Do Nothing
    }
  }

  private fun handleResendOtp(response: ResponseUI<Any>) = when (response.status) {
    ResponseUI.Status.SUCCESS -> {
      binding.progressBar.hide()
      binding.root.snackbar(getString(string.otp_sent))
    }
    ResponseUI.Status.ERROR -> {
      binding.progressBar.hide()
      binding.root.snackbar(response.message)
    }
    else -> { // Do Nothing
    }
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
