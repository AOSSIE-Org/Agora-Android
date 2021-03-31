package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.btn_verify_otp
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.cb_trusted_device
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.otp_til
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.progress_bar
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.tv_resend_otp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.network.responses.ResponseResult
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Error
import org.aossie.agoraandroid.data.network.responses.ResponseResult.Success
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

  private lateinit var rootView: View

  private var password: String? = null
  private var crypto: String? = null
  private var user: User? = null

  private val viewModel: TwoFactorAuthViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_two_factor_auth, container, false)

    password = TwoFactorAuthFragmentArgs.fromBundle(requireArguments()).password
    crypto = TwoFactorAuthFragmentArgs.fromBundle(requireArguments()).crypto

    viewModel.user.observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          user = it
        }
      }
    )

    rootView.btn_verify_otp.setOnClickListener {
      rootView.progress_bar.show()
      val otp = rootView.otp_til.editText
        ?.text
        .toString()
        .trim { it <= ' ' }
      if (otp.isEmpty()) {
        rootView.snackbar("Please Enter OTP")
        rootView.progress_bar.hide()
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        if (rootView.cb_trusted_device.isChecked) {
          viewModel.verifyOTP(
            otp, rootView.cb_trusted_device.isChecked, password!!, user!!.crypto!!
          )
        } else {
          rootView.progress_bar.hide()
          rootView.snackbar("Please, tap on the checkbox to proceed")
        }
      }
    }

    rootView.tv_resend_otp.setOnClickListener {
      if (user != null) {
        rootView.progress_bar.show()
        viewModel.resendOTP(user!!.username!!, user!!.password!!)
      } else {
        rootView.snackbar("Please try again")
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

    return rootView
  }

  private fun handleVerifyOtp(response: ResponseResult) = when (response) {
    is Success<*> -> {
      rootView.progress_bar.hide()
      Navigation.findNavController(rootView)
        .navigate(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
    }
    is Error<*> -> {
      rootView.progress_bar.hide()
      rootView.snackbar(response.error.toString())
    }
  }

  private fun handleResendOtp(response: ResponseResult) = when (response) {
    is Success<*> -> {
      rootView.progress_bar.hide()
      rootView.snackbar("OTP is sent to your registered email address")
    }
    is Error<*> -> {
      rootView.progress_bar.hide()
      rootView.snackbar(response.error.toString())
    }
  }
}
