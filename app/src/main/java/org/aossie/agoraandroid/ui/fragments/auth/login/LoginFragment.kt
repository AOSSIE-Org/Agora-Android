package org.aossie.agoraandroid.ui.fragments.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_login.view.btn_facebook_login
import kotlinx.android.synthetic.main.fragment_login.view.forgot_password_tv
import kotlinx.android.synthetic.main.fragment_login.view.login_btn
import kotlinx.android.synthetic.main.fragment_login.view.login_password_til
import kotlinx.android.synthetic.main.fragment_login.view.login_user_name_til
import kotlinx.android.synthetic.main.fragment_login.view.password
import kotlinx.android.synthetic.main.fragment_login.view.progress_bar
import kotlinx.android.synthetic.main.fragment_login.view.username
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import timber.log.Timber
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : Fragment(), AuthListener, LoginListener {

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private lateinit var rootView: View

  private var callbackManager: CallbackManager? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_login, container, false)

    loginViewModel.authListener = this
    loginViewModel.loginListener = this

    callbackManager = Factory.create()

    LoginManager.getInstance()
      .registerCallback(
        callbackManager,
        object : FacebookCallback<LoginResult?> {
          override fun onSuccess(loginResult: LoginResult?) {
            Timber.d("Success")
            prefs.setFacebookAccessToken(loginResult!!.accessToken.token)
            loginViewModel.facebookLogInRequest(loginResult.accessToken.token)
          }

          override fun onCancel() {
            Toast.makeText(context, "Login Cancel", Toast.LENGTH_LONG)
              .show()
          }

          override fun onError(exception: FacebookException) {
            Toast.makeText(context, exception.message, Toast.LENGTH_LONG)
              .show()
          }
        }
      )

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
      HideKeyboard.hideKeyboardInFrag(this)
      loginViewModel.logInRequest(userName, userPass)
    }

    rootView.password.addTextChangedListener(loginTextWatcher)
    rootView.username.addTextChangedListener(loginTextWatcher)

    rootView.btn_facebook_login.setOnClickListener {
      LoginManager.getInstance()
        .logInWithReadPermissions(
          activity,
          listOf("email", "public_profile")
        )
    }

    return rootView
  }

  private val loginTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val usernameInput: String = rootView.username.text
        .toString()
        .trim()
      val passwordInput: String = rootView.password.text
        .toString()
        .trim()
      rootView.login_btn.isEnabled = usernameInput.isNotEmpty() && passwordInput.isNotEmpty()
    }
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    Timber.d("Activity result")
    callbackManager!!.onActivityResult(requestCode, resultCode, data)
  }

  override fun onSuccess(message: String?) {
    rootView.login_btn.toggleIsEnable()
    rootView.progress_bar.hide()
    Navigation.findNavController(rootView)
      .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
  }

  override fun onStarted() {
    rootView.progress_bar.show()
    rootView.login_btn.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
    rootView.login_btn.toggleIsEnable()
  }

  override fun onTwoFactorAuthentication(password: String, crypto: String) {
    loginViewModel.getLoggedInUser().observe(
      viewLifecycleOwner,
      Observer {
        if (it != null) {
          if (it.twoFactorAuthentication!!) {
            rootView.snackbar("OTP is sent to your registered email address")
            val action = LoginFragmentDirections.actionLoginFragmentToTwoFactorAuthFragment(password, crypto)
            Navigation.findNavController(rootView)
              .navigate(action)
          }
        }
      }
    )
  }
}
