package org.aossie.agoraandroid.ui.fragments.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.auth.login.LoginScreen
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.BackArrowClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.EnteredPassword
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.EnteredUsername
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.ForgotPasswordClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.LoginClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.LoginFacebookClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.SnackBarActionClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginUiEvent.OnTwoFactorAuthentication
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginUiEvent.UserLoggedIn
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginViewModelEvent
import org.aossie.agoraandroid.ui.theme.AgoraTheme
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
) : BaseFragment(viewModelFactory) {
  private lateinit var composeView: ComposeView

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private var callbackManager: CallbackManager? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onFragmentInitiated() {
    composeView.setContent {

      val loginDataState by loginViewModel.loginDataState.collectAsState()
      val progressErrorState by loginViewModel.progressAndErrorState.collectAsState()

      val systemUiController = rememberSystemUiController()
      val useDarkIcons = !isSystemInDarkTheme()

      DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setStatusBarColor(
          color = Color.Transparent,
          darkIcons = useDarkIcons
        )
        onDispose {}
      }
      AgoraTheme {
        LoginScreen(loginDataState,progressErrorState) { event ->
          when(event){
            is EnteredPassword -> {
              loginViewModel.onEvent(LoginViewModelEvent.EnteredPassword(event.password))
            }
            is EnteredUsername -> {
              loginViewModel.onEvent(LoginViewModelEvent.EnteredUserName(event.username))
            }
            ForgotPasswordClick -> {
              findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
            }
            LoginClick -> {
              loginViewModel.onEvent(LoginViewModelEvent.LoginClick)
            }
            BackArrowClick ->{
              findNavController().navigateUp()
            }
            LoginFacebookClick -> {
              LoginManager
                .getInstance()
                .logInWithReadPermissions(
                  requireActivity(),
                  callbackManager!!,
                  listOf("email", "public_profile")
                )
            }
            SnackBarActionClick -> {
              loginViewModel.hideSnackBar()
            }
            else -> {}
          }
        }
      }
    }

    initObjects()

    initListeners()
  }

  private fun initListeners() {
    lifecycleScope.launch {
      loginViewModel.uiEvents.collectLatest { event ->
        when(event) {
          UserLoggedIn -> {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
          }
          is OnTwoFactorAuthentication -> {
            onTwoFactorAuthentication(event.crypto)
          }
        }
      }
    }
  }

  private fun initObjects() {
    loginViewModel.sessionExpiredListener = this
    callbackManager = CallbackManager.Factory.create()

    LoginManager.getInstance()
      .registerCallback(
        callbackManager,
        object : FacebookCallback<LoginResult> {
          override fun onSuccess(loginResult: LoginResult) {
            Timber.d("Success")
            lifecycleScope.launch {
              prefs.setFacebookAccessToken(loginResult?.accessToken?.token)
            }
            loginViewModel.facebookLogInRequest()
          }

          override fun onCancel() {
            loginViewModel.showError(resources.getString(R.string.login_cancelled))
          }

          override fun onError(exception: FacebookException) {
            notify(exception.message.toString())
            loginViewModel.showError(exception.message.toString())
          }
        }
      )
  }

  override fun onSessionExpired() {
    // do nothing
  }

  fun onTwoFactorAuthentication(
    crypto: String
  ) {
    lifecycleScope.launch {
      loginViewModel.getLoggedInUser()
        .collect {
          if (it != null) {
            if (it.twoFactorAuthentication!!) {
              notify(getString(R.string.otp_sent))
              val action = LoginFragmentDirections.actionLoginFragmentToTwoFactorAuthFragment(crypto)
              findNavController().navigate(action)
            }
          }
        }
    }
  }
}
