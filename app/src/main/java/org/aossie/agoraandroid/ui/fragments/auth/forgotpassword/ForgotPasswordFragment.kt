package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_forgot_password.view.button_send_link
import kotlinx.android.synthetic.main.fragment_forgot_password.view.edit_text_user_name
import kotlinx.android.synthetic.main.fragment_forgot_password.view.progress_bar
import org.aossie.agoraandroid.R
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

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_forgot_password, container, false)

    rootView.button_send_link.setOnClickListener {
      val userName = rootView.edit_text_user_name.editText
        ?.text
        .toString()
        .trim { it <= ' ' }
      if (userName.isEmpty()) {
        rootView.snackbar("Please Enter User Name")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        rootView.progress_bar.show()
        forgotPasswordViewModel.sendResetLink(userName)
      }
    }

    forgotPasswordViewModel.getSendResetLinkLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> rootView.progress_bar.show()
          ResponseUI.Status.SUCCESS -> {
            rootView.progress_bar.hide()
            rootView.snackbar(requireContext().getString(R.string.link_sent_please_check_your_email))
          }
          ResponseUI.Status.ERROR -> {
            rootView.progress_bar.hide()
            rootView.snackbar(it.message ?: "")
          }
        }
      }
    )

    return rootView
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
