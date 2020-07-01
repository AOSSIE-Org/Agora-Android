package org.aossie.agoraandroid.ui.fragments.auth.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_forgot_password.view.button_send_link
import kotlinx.android.synthetic.main.fragment_forgot_password.view.edit_text_user_name
import kotlinx.android.synthetic.main.fragment_forgot_password.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
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
  ): Fragment(), AuthListener {

  private val forgotPasswordViewModel: ForgotPasswordViewModel by viewModels {
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

    forgotPasswordViewModel.authListener = this

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

    return rootView
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    rootView.snackbar(context!!.getString(R.string.link_sent_please_check_your_email))
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
  }

}
