package org.aossie.agoraandroid.ui.fragments.auth.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
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
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentLoginBinding
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.disableView
import org.aossie.agoraandroid.utilities.enableView
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

  private lateinit var binding: FragmentLoginBinding

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private var callbackManager: CallbackManager? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

    initObjects()

    initListeners()

    return binding.root
  }

  private fun initObjects() {
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
              binding.btnFacebookLogin.enableView()
              Toast.makeText(context, "Login Cancel", Toast.LENGTH_LONG)
                .show()
            }

            override fun onError(exception: FacebookException) {
              binding.btnFacebookLogin.enableView()
              Toast.makeText(context, exception.message, Toast.LENGTH_LONG)
                .show()
            }
          }
        )
  }

  private fun initListeners() {
    binding.forgotPasswordTv.setOnClickListener {
      Navigation.findNavController(binding.root)
          .navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
    }

    binding.loginBtn.setOnClickListener {
      val userName = binding.loginUserNameTil.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      val userPass = binding.loginPasswordTil.editText
          ?.text
          .toString()
          .trim { it <= ' ' }
      HideKeyboard.hideKeyboardInFrag(this)
      loginViewModel.logInRequest(userName, userPass)
    }

    binding.password.addTextChangedListener(loginTextWatcher)
    binding.username.addTextChangedListener(loginTextWatcher)

    binding.btnFacebookLogin.setOnClickListener {
      binding.btnFacebookLogin.disableView()
      LoginManager.getInstance()
          .logInWithReadPermissions(
            activity,
            listOf("email", "public_profile")
          )
    }
  }

  private val loginTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(
      s: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int
    ) {
      val usernameInput: String = binding.username.text
        .toString()
        .trim()
      val passwordInput: String = binding.password.text
        .toString()
        .trim()
      binding.loginBtn.isEnabled = usernameInput.isNotEmpty() && passwordInput.isNotEmpty()
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
    binding.loginBtn.toggleIsEnable()
    binding.progressBar.hide()
    Navigation.findNavController(binding.root)
      .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
  }

  override fun onStarted() {
    binding.progressBar.show()
    binding.loginBtn.toggleIsEnable()
  }

  override fun onFailure(message: String) {
    binding.progressBar.hide()
    binding.root.snackbar(message)
    binding.loginBtn.toggleIsEnable()
    binding.btnFacebookLogin.enableView()
  }

  override fun onTwoFactorAuthentication(
    password: String,
    crypto: String
  ) {
    loginViewModel.getLoggedInUser()
      .observe(
        viewLifecycleOwner,
        Observer {
          if (it != null) {
            if (it.twoFactorAuthentication!!) {
              binding.root.snackbar("OTP is sent to your registered email address")
              val action =
                LoginFragmentDirections.actionLoginFragmentToTwoFactorAuthFragment(password, crypto)
              Navigation.findNavController(binding.root)
                .navigate(action)
            }
          }
        }
      )
  }
}
