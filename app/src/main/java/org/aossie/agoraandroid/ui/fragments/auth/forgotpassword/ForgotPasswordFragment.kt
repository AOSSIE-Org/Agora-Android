package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentForgotPasswordBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
    viewModelFactory
  }
  private lateinit var binding: FragmentForgotPasswordBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentForgotPasswordBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {

    binding.buttonSendLink.setOnClickListener {
      val userName = binding.editTextUserName.editText?.text.toString().trim()
      if (userName.isEmpty()) {
        notify("Please Enter User Name")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        binding.progressBar.show()
        forgotPasswordViewModel.sendResetLink(userName)
      }
    }

    lifecycleScope.launch {
      forgotPasswordViewModel.getSendResetLinkStateFlow.collect {
        if (it != null) {
          when (it.status) {
            ResponseUI.Status.LOADING -> binding.progressBar.show()
            ResponseUI.Status.SUCCESS -> {
              binding.progressBar.hide()
              notify(getString(R.string.link_sent_please_check_your_email))
            }
            ResponseUI.Status.ERROR -> {
              binding.progressBar.hide()
              notify(it.message)
            }
          }
        }
      }
    }
  }
}
