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
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.dto.NewUserDto
import org.aossie.agoraandroid.data.dto.SecurityQuestionDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(), SessionExpiredListener {

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
    signUpViewModel.sessionExpiredListener = this

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

    signUpViewModel.getSignUpLiveData.observe(viewLifecycleOwner,{
      when(it.status){
        ResponseUI.Status.LOADING ->  {
          rootView.progress_bar.show()
          rootView.signup_btn.toggleIsEnable()
        }
        ResponseUI.Status.SUCCESS ->{
          rootView.progress_bar.hide()
          rootView.snackbar(getString(string.verify_account))
          rootView.signup_btn.toggleIsEnable()
        }
        ResponseUI.Status.ERROR ->{
          rootView.snackbar(it.message?:"")
          rootView.progress_bar.hide()
          rootView.signup_btn.toggleIsEnable()
        }
      }
    })

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
    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail)
      .matches()
    ) {
      signup_email.error = "Enter a valid email address!!!"
    } else {
      signup_email.error = null
      signUpViewModel.signUpRequest(NewUserDto(userEmail, firstName, userName, lastName, userPass, SecurityQuestionDto(securityQuestionAnswer, "", securityQuestion!!)))
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
      rootView.signup_btn.isEnabled = usernameInput.isNotEmpty() &&
        passwordInput.isNotEmpty() &&
        firstNameInput.isNotEmpty() &&
        lastNameInput.isNotEmpty() &&
        emailInput.isNotEmpty() &&
        answerInput.isNotEmpty()
    }
  }



  override fun onSessionExpired() {
    // do nothing
  }
}
