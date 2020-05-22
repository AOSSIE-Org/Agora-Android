package org.aossie.agoraandroid.ui.fragments.auth.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_sign_up.security_answer
import kotlinx.android.synthetic.main.fragment_sign_up.signup_email
import kotlinx.android.synthetic.main.fragment_sign_up.signup_first_name
import kotlinx.android.synthetic.main.fragment_sign_up.signup_last_name
import kotlinx.android.synthetic.main.fragment_sign_up.signup_password
import kotlinx.android.synthetic.main.fragment_sign_up.signup_user_name
import kotlinx.android.synthetic.main.fragment_sign_up.view.sign_up_security_question
import kotlinx.android.synthetic.main.fragment_sign_up.view.signup_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.showActionBar

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment() {

  private var signUpViewModel: SignUpViewModel? = null
  private var securityQuestionOfSignUp: String? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_sign_up, container, false)
    showActionBar()
    signUpViewModel = SignUpViewModel(activity!!.application, context!!)
    rootView.signup_btn.setOnClickListener {
      HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
      validateAllFields()
    }
    val adapter = ArrayAdapter.createFromResource(
        context!!, array.security_questions,
        android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    rootView.sign_up_security_question.setAdapter(adapter)
    rootView.sign_up_security_question.setOnItemSelectedListener(object : OnItemSelectedListener {
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
      signUpViewModel!!.signUpRequest(
          userName, userPass, userEmail, firstName, lastName,
          securityQuestion, securityQuestionAnswer
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

}
