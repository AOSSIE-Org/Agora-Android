package org.aossie.agoraandroid.ui.fragments.auth.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_login.view.password
import kotlinx.android.synthetic.main.fragment_login.view.username
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.network.Api
import org.aossie.agoraandroid.data.network.NetworkInterceptor
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.showActionBar

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), AuthListener {

  private var loginViewModel: LoginViewModel? = null

  private lateinit var rootView: View

  private lateinit var loadToast: LoadToast

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_login, container, false)
    showActionBar()

    loadToast = LoadToast(context)
    val networkConnectionInterceptor = NetworkInterceptor(context!!)
    val userRepository = UserRepository(Api(networkConnectionInterceptor))
    loginViewModel = LoginViewModel(userRepository, activity!!.application)
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
      HideKeyboard.hideKeyboardInFrag(this)
      loginViewModel?.logInRequest(userName, userPass)
    }

    rootView.password.addTextChangedListener(loginTextWatcher)
    rootView.username.addTextChangedListener(loginTextWatcher)

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

  override fun onSuccess() {
    loadToast.success()
    Navigation.findNavController(rootView)
        .navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
  }

  override fun onStarted() {
    loadToast.setText("Logging in")
    loadToast.show()
  }

  override fun onFailure(message: String) {
    loadToast.error()
  }

}
