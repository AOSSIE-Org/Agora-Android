package org.aossie.agoraandroid.ui.fragments.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.view.forgot_password_tv
import kotlinx.android.synthetic.main.fragment_login.view.login_btn
import kotlinx.android.synthetic.main.fragment_login.view.login_password_til
import kotlinx.android.synthetic.main.fragment_login.view.login_user_name_til
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.showActionBar

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), AuthListener {

  private var loginViewModel: LoginViewModel? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView =  inflater.inflate(R.layout.fragment_login, container, false)
    showActionBar()
    loginViewModel = LoginViewModel(activity!!.application, context!!)
    loginViewModel?.authListener = this
    rootView.forgot_password_tv.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
    }
    rootView.login_btn.setOnClickListener {
      val userName = rootView.login_user_name_til.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      val userPass = rootView.login_password_til.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      if (userName.isEmpty()) {
        rootView.login_user_name_til.setError("Please enter User Name")
      } else {
        rootView.login_user_name_til.setError(null)
      }
      if (userPass.isEmpty()) {
        rootView.login_password_til.setError("Please enter password")
      } else {
        HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
        rootView.login_password_til.setError(null)
        loginViewModel?.logInRequest(userName, userPass)
      }
    }

    return rootView
  }

  override fun onSuccess() {
    Navigation.findNavController(rootView)
        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
  }

}
