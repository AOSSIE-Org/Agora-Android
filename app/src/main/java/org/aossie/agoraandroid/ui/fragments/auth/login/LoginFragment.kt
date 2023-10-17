package org.aossie.agoraandroid.ui.fragments.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.ForgotPasswordClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginScreenEvent.LoginFacebookClick
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginUiEvent.OnTwoFactorAuthentication
import org.aossie.agoraandroid.ui.screens.auth.login.events.LoginUiEvent.UserLoggedIn
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.navigateSafely
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

      LaunchedEffect(key1 = Unit) {
        loginViewModel.uiEvents.collectLatest { event ->
          when(event) {
            UserLoggedIn -> {
              findNavController().navigateSafely(
                LoginFragmentDirections.actionLoginFragmentToHomeFragment()
              )
            }
            is OnTwoFactorAuthentication -> {
              findNavController().navigateSafely(
                LoginFragmentDirections.actionLoginFragmentToTwoFactorAuthFragment(event.crypto)
              )
            }
          }
        }
      }

      AgoraTheme {
        LoginScreen(loginDataState,progressErrorState) { event ->
          when(event){
            ForgotPasswordClick -> {
              findNavController()
                .navigateSafely(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
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
            else -> {
              loginViewModel.onEvent(event)
            }
          }
        }
      }
    }
    initObjects()
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
            loginViewModel.showMessage(resources.getString(R.string.login_cancelled))
          }

          override fun onError(exception: FacebookException) {
            notify(exception.message.toString())
            loginViewModel.showMessage(exception.message.toString())
          }
        }
      )
  }
}
