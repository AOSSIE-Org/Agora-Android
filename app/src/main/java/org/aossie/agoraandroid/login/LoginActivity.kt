package org.aossie.agoraandroid.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_login.*
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.forgotpassword.ForgotPasswordSend
import org.aossie.agoraandroid.home.HomeActivity
import org.aossie.agoraandroid.networking.Networking
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.CacheManager.Companion.getInstance
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.State

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var loadToast: LoadToast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val viewModelFactory = LoginViewModelFactory(Networking(RetrofitClient.getAPIService()), getInstance(SharedPrefs(this)))

        loginViewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        loadToast = LoadToast(this)

        forgot_password_tv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordSend::class.java))
        }

        login_btn.setOnClickListener {
            val userName = login_user_name_til.editText!!.text.toString().trim()
            val userPass = login_password_til.editText!!.text.toString().trim()
            if (userName.isEmpty()) {
                login_user_name_til.error = "Please enter User Name"
            } else {
                login_user_name_til.error = null
            }
            if (userPass.isEmpty()) {
                login_password_til.error = "Please enter password"
            } else {
                login_password_til.error = null
                loginViewModel.logInRequest(userName, userPass)
            }
        }

        loginViewModel.loginState.observe(this, Observer {
            when (it) {
                State.Normal -> {
                    Log.d("loginState", "Normal")
                }
                State.Loading -> {
                    loadToast.setText("Logging in")
                    loadToast.show()
                }
                State.Success -> {
                    loadToast.success()
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.addCategory(Intent.CATEGORY_HOME)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
                State.Error -> {
                    val error = it.errors.getString("error")
                    loadToast.error()
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    Log.d("loginError", "$error")
                }
            }
        })

        loginViewModel.facebookState.observe(this, Observer {
            when (it) {
                State.Normal -> {
                    Log.d("facebookState", "Normal")
                }
                State.Loading -> {
                    Log.d("facebookState", "Loading")
                }
                State.Success -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                State.Error -> {
                    val error = it.errors.getString("error")
                    loadToast.error()
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                    Log.d("loginError", "$error")
                }
            }
        })
    }
}