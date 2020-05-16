package org.aossie.agoraandroid.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.facebook.AccessToken
import com.facebook.AccessTokenTracker
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.fragment_welcome.view.fb_login_btn
import kotlinx.android.synthetic.main.fragment_welcome.view.signin_btn
import kotlinx.android.synthetic.main.fragment_welcome.view.signup_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.login.LoginActivity
import org.aossie.agoraandroid.login.LoginViewModel
import org.aossie.agoraandroid.signup.SignUpActivity
import java.util.Arrays

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

  private var callbackManager: CallbackManager? = null
  private var loginViewModel: LoginViewModel? = null
  var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
    override fun onCurrentAccessTokenChanged(
      oldAccessToken: AccessToken,
      currentAccessToken: AccessToken
    ) {
      if (currentAccessToken == null) {
        Toast.makeText(context, "User Logged Out", Toast.LENGTH_SHORT)
            .show()
      } else {
        val facebookAccessToken = currentAccessToken.token
        loginViewModel!!.facebookLogInRequest(facebookAccessToken)
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    val rootView = inflater.inflate(R.layout.fragment_welcome, container, false)
    loginViewModel = LoginViewModel(activity!!.application, context)
    callbackManager = Factory.create()
    LoginManager.getInstance()
        .registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
              override fun onSuccess(loginResult: LoginResult?) {
                Log.d("Success", "Login")
              }

              override fun onCancel() {
                Toast.makeText(context, "Login Cancel", Toast.LENGTH_LONG)
                    .show()
              }

              override fun onError(exception: FacebookException) {
                Toast.makeText(context, exception.message, Toast.LENGTH_LONG)
                    .show()
              }
            })
    rootView.fb_login_btn.setOnClickListener {
      LoginManager.getInstance()
          .logInWithReadPermissions(
              activity,
              Arrays.asList("public_profile", "user_friends")
          )
    }
    rootView.signin_btn.setOnClickListener {
      val logInIntent = Intent(context, LoginActivity::class.java)
      logInIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      startActivity(logInIntent)
    }
    rootView.signup_btn.setOnClickListener {
      val signUpIntent = Intent(context, SignUpActivity::class.java)
      startActivity(signUpIntent)
    }

    return rootView
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    callbackManager!!.onActivityResult(requestCode, resultCode, data)
    super.onActivityResult(requestCode, resultCode, data)
  }

}
