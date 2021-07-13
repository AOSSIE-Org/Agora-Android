package org.aossie.agoraandroid.ui.fragments.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentLoginBinding
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
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
) : Fragment(), SessionExpiredListener {

  private lateinit var binding: FragmentLoginBinding

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private var callbackManager: CallbackManager? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentLoginBinding.inflate(layoutInflater)

    initObjects()

    initListeners()

    return binding.root
  }

  private fun initObjects() {
    loginViewModel.sessionExpiredListener = this
    loginViewModel.getLoginLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> {
            binding.progressBar.show()
            binding.loginBtn.toggleIsEnable()
          }
          ResponseUI.Status.SUCCESS -> {
            binding.loginBtn.toggleIsEnable()
            binding.progressBar.hide()
            it.message?.let { crypto ->
              onTwoFactorAuthentication(crypto)
            } ?: kotlin.run {
              Navigation.findNavController(binding.root)
                .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
            }
          }
          ResponseUI.Status.ERROR -> {
            binding.progressBar.hide()
            binding.root.snackbar(it.message)
            binding.loginBtn.toggleIsEnable()
            enableBtnFacebook()
          }
        }
      }
    )

    callbackManager = Factory.create()

    LoginManager.getInstance()
      .registerCallback(
        callbackManager,
        object : FacebookCallback<LoginResult?> {
          override fun onSuccess(loginResult: LoginResult?) {
            Timber.d("Success")
            lifecycleScope.launch {
              prefs.setFacebookAccessToken(loginResult?.accessToken?.token)
            }
            loginViewModel.facebookLogInRequest()
          }

          override fun onCancel() {
            enableBtnFacebook()
            binding.root.snackbar(resources.getString(R.string.login_cancelled))
          }

          override fun onError(exception: FacebookException) {
            enableBtnFacebook()
            binding.root.snackbar(exception.message.toString())
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

    binding.password.doAfterTextChanged {
      updateLoginButton()
    }
    binding.username.doAfterTextChanged {
      updateLoginButton()
    }

    binding.btnFacebookLogin.setOnClickListener {
      binding.btnFacebookLogin.disableView()
      LoginManager.getInstance()
        .logInWithReadPermissions(
          activity,
          listOf("email", "public_profile")
        )
    }
  }

  private fun updateLoginButton() {
    val usernameInput: String = binding.username.text
      .toString()
      .trim()
    val passwordInput: String = binding.password.text
      .toString()
      .trim()
    binding.loginBtn.isEnabled = usernameInput.isNotEmpty() && passwordInput.isNotEmpty()
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    Timber.d("Activity result")
    callbackManager!!.onActivityResult(requestCode, resultCode, data)
  }

  override fun onSessionExpired() {
    // do nothing
  }

  private fun enableBtnFacebook() {
    binding.btnFacebookLogin.enableView()
  }

  fun onTwoFactorAuthentication(
    crypto: String
  ) {
    loginViewModel.getLoggedInUser()
      .observe(
        viewLifecycleOwner,
        Observer {
          if (it != null) {
            if (it.twoFactorAuthentication!!) {
              binding.root.snackbar(getString(R.string.otp_sent))
              val action =
                LoginFragmentDirections.actionLoginFragmentToTwoFactorAuthFragment(crypto)
              Navigation.findNavController(binding.root)
                .navigate(action)
            }
          }
        }
      )
  }
}
