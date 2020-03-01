package org.aossie.agoraandroid.homelogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.login.LoginActivity
import org.aossie.agoraandroid.login.LoginViewModel
import org.aossie.agoraandroid.signup.SignUpActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class HomeLoginActivity : AppCompatActivity() {
    private var callbackManager: CallbackManager? = null
    private val loginViewModel: LoginViewModel by viewModel()
    var accessTokenTracker: AccessTokenTracker = object : AccessTokenTracker() {
        override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken,
                                                 currentAccessToken: AccessToken) {
            if (currentAccessToken == null) {
                Toast.makeText(this@HomeLoginActivity, "User Logged Out", Toast.LENGTH_SHORT).show()
            } else {
                val facebookAccessToken = currentAccessToken.token
                loginViewModel!!.facebookLogInRequest(facebookAccessToken)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_login)
        val signIn = findViewById<Button>(R.id.signin_btn)
        val signUp = findViewById<Button>(R.id.signup_btn)
        FacebookSdk.sdkInitialize(this.applicationContext)
        callbackManager = CallbackManager.Factory.create()
        val facebookLogin = findViewById<Button>(R.id.fb_login_btn)
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult?> {
                    override fun onSuccess(loginResult: LoginResult?) {
                        Log.d("Success", "Login")
                    }

                    override fun onCancel() {
                        Toast.makeText(this@HomeLoginActivity, "Login Cancel", Toast.LENGTH_LONG).show()
                    }

                    override fun onError(exception: FacebookException) {
                        Toast.makeText(this@HomeLoginActivity, exception.message, Toast.LENGTH_LONG)
                                .show()
                    }
                })
        facebookLogin.setOnClickListener {
            LoginManager.getInstance()
                    .logInWithReadPermissions(this@HomeLoginActivity,
                            Arrays.asList("public_profile", "user_friends"))
        }
        signIn.setOnClickListener {
            val logInIntent = Intent(this@HomeLoginActivity, LoginActivity::class.java)
            startActivity(logInIntent)
        }
        signUp.setOnClickListener {
            val signUpIntent = Intent(this@HomeLoginActivity, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager!!.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}