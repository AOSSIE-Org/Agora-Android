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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.dto.NewUserDto
import org.aossie.agoraandroid.data.dto.SecurityQuestionDto
import org.aossie.agoraandroid.databinding.FragmentSignUpBinding
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

  private lateinit var binding: FragmentSignUpBinding

  private val signUpViewModel: SignUpViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
    signUpViewModel.sessionExpiredListener = this

    binding.signupBtn.setOnClickListener {
      HideKeyboard.hideKeyboardInActivity(activity as AppCompatActivity)
      validateAllFields()
    }

    val adapter = ArrayAdapter.createFromResource(
      requireContext(), array.security_questions,
      android.R.layout.simple_spinner_item
    )
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

    binding.signUpSecurityQuestion.adapter = adapter

    binding.signUpSecurityQuestion.onItemSelectedListener = object : OnItemSelectedListener {
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
    binding.etUsername.addTextChangedListener(signUpTextWatcher)
    binding.etFirstName.addTextChangedListener(signUpTextWatcher)
    binding.etLastName.addTextChangedListener(signUpTextWatcher)
    binding.etEmail.addTextChangedListener(signUpTextWatcher)
    binding.etAnswer.addTextChangedListener(signUpTextWatcher)
    binding.etPassword.addTextChangedListener(signUpTextWatcher)

    signUpViewModel.getSignUpLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> {
            binding.progressBar.show()
            binding.signupBtn.toggleIsEnable()
          }
          ResponseUI.Status.SUCCESS -> {
            binding.progressBar.hide()
            binding.root.snackbar(getString(string.verify_account))
            binding.signupBtn.toggleIsEnable()
          }
          ResponseUI.Status.ERROR -> {
            binding.root.snackbar(it.message)
            binding.progressBar.hide()
            binding.signupBtn.toggleIsEnable()
          }
        }
      }
    )

    return binding.root
  }

  private fun validateAllFields() {
    val userName = binding.signupUserName.editText
      ?.text
      .toString()
    val firstName = binding.signupFirstName.editText
      ?.text
      .toString()
    val lastName = binding.signupLastName.editText
      ?.text
      .toString()
    val userEmail = binding.signupEmail.editText
      ?.text
      .toString()
    val userPass = binding.signupPassword.editText
      ?.text
      .toString()
    val securityQuestionAnswer = binding.securityAnswer.editText
      ?.text
      .toString()
    val securityQuestion = securityQuestionOfSignUp
    if (!Patterns.EMAIL_ADDRESS.matcher(userEmail)
      .matches()
    ) {
      binding.signupEmail.error = "Enter a valid email address!!!"
    } else {
      binding.signupEmail.error = null
      signUpViewModel.signUpRequest(
        NewUserDto(
          userEmail, firstName, userName, lastName, userPass,
          SecurityQuestionDto(securityQuestionAnswer, "", securityQuestion!!)
        )
      )
    }
  }

  private val signUpTextWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(
      s: CharSequence,
      start: Int,
      count: Int,
      after: Int
    ) {
    }

    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(
      s: CharSequence,
      start: Int,
      before: Int,
      count: Int
    ) {
      val usernameInput: String = binding.etUsername.text
        .toString()
        .trim()
      val passwordInput: String = binding.etPassword.text
        .toString()
        .trim()
      val firstNameInput: String = binding.etFirstName.text
        .toString()
        .trim()
      val lastNameInput: String = binding.etLastName.text
        .toString()
        .trim()
      val emailInput: String = binding.etEmail.text
        .toString()
        .trim()
      val answerInput: String = binding.etAnswer.text
        .toString()
        .trim()
      binding.signupBtn.isEnabled = usernameInput.isNotEmpty() &&
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
