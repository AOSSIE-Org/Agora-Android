package org.aossie.agoraandroid.ui.fragments.auth.signup

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.fragment_sign_up.security_answer
import kotlinx.android.synthetic.main.fragment_sign_up.signup_email
import kotlinx.android.synthetic.main.fragment_sign_up.signup_first_name
import kotlinx.android.synthetic.main.fragment_sign_up.signup_last_name
import kotlinx.android.synthetic.main.fragment_sign_up.signup_password
import kotlinx.android.synthetic.main.fragment_sign_up.signup_user_name
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_answer
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_email
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_first_name
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_last_name
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_password
import kotlinx.android.synthetic.main.fragment_sign_up.view.et_username
import kotlinx.android.synthetic.main.fragment_sign_up.view.progress_bar
import kotlinx.android.synthetic.main.fragment_sign_up.view.sign_up_security_question
import kotlinx.android.synthetic.main.fragment_sign_up.view.signup_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment
  @Inject
  constructor(
    private val viewModelFactory: ViewModelProvider.Factory
  ): Fragment(), AuthListener {

  private var securityQuestionOfSignUp: String? = null

  private lateinit var rootView: View

  private val signUpViewModel: SignUpViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
    signUpViewModel.authListener = this

    rootView.signup_btn.setOnClickListener {
      HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
      validateAllFields()
    }

    val adapter = ArrayAdapter.createFromResource(
        requireContext(), array.security_questions,
        android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    rootView.sign_up_security_question.adapter = adapter

    rootView.sign_up_security_question.onItemSelectedListener = object : OnItemSelectedListener {
      override fun onItemSelected(
        adapterView: AdapterView<*>,
        view: View,
        i: Int,
        l: Long
      ) {
        securityQuestionOfSignUp = adapterView.getItemAtPosition(i)
            .toString()
      }

      override fun onNothingSelected(adapterView: AdapterView<*>?) {
        securityQuestionOfSignUp = resources.getStringArray(array.security_questions)[0]
      }
    }

    // Attach text watcher to different text input edit texts
    rootView.et_username.addTextChangedListener(signUpTextWatcher)
    rootView.et_first_name.addTextChangedListener(signUpTextWatcher)
    rootView.et_last_name.addTextChangedListener(signUpTextWatcher)
    rootView.et_email.addTextChangedListener(signUpTextWatcher)
    rootView.et_answer.addTextChangedListener(signUpTextWatcher)
    rootView.et_password.addTextChangedListener(signUpTextWatcher)

    return rootView
  }

  private fun validateAllFields() {
    val userName = signup_user_name.editText
        ?.text
        .toString()
    val firstName = signup_first_name.editText
        ?.text
        .toString()
    val lastName = signup_last_name.editText
        ?.text
        .toString()
    val userEmail = signup_email.editText
        ?.text
        .toString()
    val userPass = signup_password.editText
        ?.text
        .toString()
    val securityQuestionAnswer = security_answer.editText
        ?.text
        .toString()
    val securityQuestion = securityQuestionOfSignUp
    validateUsername(userName)
    if (firstName.isEmpty()) {
      signup_first_name.error = "Please enter First Name"
    } else {
      signup_first_name.error = null
    }
    if (lastName.isEmpty()) {
      signup_last_name.error = "Please enter Last Name"
    } else {
      signup_last_name.error = null
    }
    if (userEmail.isEmpty()) {
      signup_email.error = "Please enter Email Address"
    } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail)
            .matches()
    ) {
      signup_email.error = "Enter a valid email address!!!"
      return
    } else {
      signup_email.error = null
    }
    if (securityQuestionAnswer.isEmpty()) {
      security_answer.error = "Please enter Security Answer"
    } else {
      security_answer.error = null
    }
    validatePassword(
        userName, firstName, lastName, userEmail, userPass, securityQuestionAnswer,
        securityQuestion
    )
  }

  private fun validatePassword(
    userName: String,
    firstName: String,
    lastName: String,
    userEmail: String,
    userPass: String,
    securityQuestionAnswer: String,
    securityQuestion: String?
  ) {
    if (userPass.isEmpty()) {
      signup_password.error = "Please enter password"
    } else {
      signup_password.error = null
      signUpViewModel.signUpRequest(
          userName, userPass, userEmail, firstName, lastName,
          securityQuestion!!, securityQuestionAnswer
      )
    }
  }

  private fun validateUsername(userName: String) {
    if (userName.isEmpty()) {
      signup_user_name.error = "Please enter User Name"
    } else {
      signup_user_name.error = null
    }
  }

  private val signUpTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val usernameInput: String = rootView.et_username.text
          .toString()
          .trim()
      val passwordInput: String = rootView.et_password.text
          .toString()
          .trim()
      val firstNameInput: String = rootView.et_first_name.text
          .toString()
          .trim()
      val lastNameInput: String = rootView.et_last_name.text
          .toString()
          .trim()
      val emailInput: String = rootView.et_email.text
          .toString()
          .trim()
      val answerInput: String = rootView.et_answer.text
          .toString()
          .trim()
      rootView.signup_btn.isEnabled = usernameInput.isNotEmpty()
          && passwordInput.isNotEmpty()
          && firstNameInput.isNotEmpty()
          && lastNameInput.isNotEmpty()
          && emailInput.isNotEmpty()
          && answerInput.isNotEmpty()
    }
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    rootView.snackbar("An activation link has been sent to registered email id to verify your account")
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onFailure(message: String) {
    rootView.snackbar(message)
    rootView.progress_bar.hide()
  }

}
