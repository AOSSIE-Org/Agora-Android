package org.aossie.agoraandroid.ui.fragments.auth.twoFactorAuthentication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.password
import kotlinx.android.synthetic.main.fragment_two_factor_auth.cb_trusted_device
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.btn_verify_otp
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.cb_trusted_device
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.otp_til
import kotlinx.android.synthetic.main.fragment_two_factor_auth.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.displayelections.VotersFragmentArgs
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

class TwoFactorAuthFragment
  @Inject
  constructor(
    private val viewModelFactory: ViewModelProvider.Factory
  ): Fragment(), AuthListener {

  private lateinit var rootView: View

  private var password: String ?= null
  private var crypto: String ?= null

  private val viewModel: TwoFactorAuthViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_two_factor_auth, container, false)

    viewModel.authListener = this

    password = TwoFactorAuthFragmentArgs.fromBundle(arguments!!).password
    crypto = TwoFactorAuthFragmentArgs.fromBundle(arguments!!).crypto

    rootView.btn_verify_otp.setOnClickListener {
      val otp = rootView.otp_til.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      if (otp.isEmpty()) {
        rootView.snackbar("Please Enter OTP")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        if(rootView.cb_trusted_device.isChecked) {
          viewModel.verifyOTP(otp, rootView.cb_trusted_device.isChecked, password!!, crypto!!)
        }else{
          rootView.snackbar("Please, tap on the checkbox to proceed")
        }
      }
    }

    return rootView
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    Log.d("friday", "success")
    Navigation.findNavController(rootView)
        .navigate(TwoFactorAuthFragmentDirections.actionTwoFactorAuthFragmentToHomeFragment())
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
  }
}