package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.SessionExpired
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
import org.aossie.agoraandroid.databinding.FragmentTwoFactorAuthBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class TwoFactorAuthFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

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
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_two_factor_auth, container, false)

    crypto = TwoFactorAuthFragmentArgs.fromBundle(requireArguments()).crypto

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
        binding.root.snackbar("Please Enter OTP")
        binding.progressBar.hide()
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        if (binding.cbTrustedDevice.isChecked) {
          viewModel.verifyOTP(
            otp, binding.cbTrustedDevice.isChecked, user!!.crypto!!
          )
        } else {
          binding.progressBar.hide()
          binding.root.snackbar("Please, tap on the checkbox to proceed")
        }
      }
    }

    binding.tvResendOtp.setOnClickListener {
      if (user != null) {
        binding.progressBar.show()
        viewModel.resendOTP(user!!.username!!)
      } else {
        binding.root.snackbar("Please try again")
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

  private fun handleVerifyOtp(response: ResponseResult) = when (response) {
    is Success -> {
      binding.progressBar.hide()
      Navigation.findNavController(binding.root)
        .navigate(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
    }
    is Error -> {
      binding.progressBar.hide()
      binding.root.snackbar(response.error.toString())
    }
    is SessionExpired -> {
      hostViewModel.setLogout(true)
    }
  }

  private fun handleResendOtp(response: ResponseResult) = when (response) {
    is Success -> {
      binding.progressBar.hide()
      binding.root.snackbar("OTP is sent to your registered email address")
    }
    is Error -> {
      binding.progressBar.hide()
      binding.root.snackbar(response.error.toString())
    }
    is SessionExpired -> {
      hostViewModel.setLogout(true)
    }
  }
}
