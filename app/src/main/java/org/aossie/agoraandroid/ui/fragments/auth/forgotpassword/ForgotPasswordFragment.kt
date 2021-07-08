package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.FragmentForgotPasswordBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(), SessionExpiredListener {

  private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var binding: FragmentForgotPasswordBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false)

    binding.buttonSendLink.setOnClickListener {
      val userName = binding.editTextUserName.editText?.text.toString().trim()
      if (userName.isEmpty()) {
        binding.root.snackbar("Please Enter User Name")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        binding.progressBar.show()
        forgotPasswordViewModel.sendResetLink(userName)
      }
    }

    forgotPasswordViewModel.getSendResetLinkLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> binding.progressBar.show()
          ResponseUI.Status.SUCCESS -> {
            binding.progressBar.hide()
            binding.root.snackbar(getString(R.string.link_sent_please_check_your_email))
          }
          ResponseUI.Status.ERROR -> {
            binding.progressBar.hide()
            binding.root.snackbar(it.message ?: "")
          }
        }
      }
    )

    return binding.root
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
