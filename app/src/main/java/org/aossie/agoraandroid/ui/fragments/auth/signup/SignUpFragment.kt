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
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.array
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.network.dto.SecurityQuestionDto
import org.aossie.agoraandroid.databinding.FragmentSignUpBinding
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.HideKeyboard
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.toggleIsEnable
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private var securityQuestionOfSignUp: String? = null

  private val signUpViewModel: SignUpViewModel by viewModels {
    viewModelFactory
  }
  private lateinit var binding: FragmentSignUpBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentSignUpBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {

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
        view: View?,
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
    binding.etUsername.doAfterTextChanged { doAfterTextChange() }
    binding.etFirstName.doAfterTextChanged { doAfterTextChange() }
    binding.etLastName.doAfterTextChanged { doAfterTextChange() }
    binding.etEmail.doAfterTextChanged { doAfterTextChange() }
    binding.etAnswer.doAfterTextChanged { doAfterTextChange() }
    binding.etPassword.doAfterTextChanged { doAfterTextChange() }

    lifecycleScope.launch {
      signUpViewModel.getSignUpStateFlow.collect {
        if (it != null) {
          when (it.status) {
            ResponseUI.Status.LOADING -> {
              binding.progressBar.show()
              makeFieldsToggleEnable()
            }
            ResponseUI.Status.SUCCESS -> {
              binding.progressBar.hide()
              notify(getString(string.verify_account))
              makeFieldsToggleEnable()
            }
            ResponseUI.Status.ERROR -> {
              notify(it.message)
              binding.progressBar.hide()
              makeFieldsToggleEnable()
            }
            else -> {}
          }
        }
      }
    }
  }

  private fun makeFieldsToggleEnable() {
    binding.signupBtn.toggleIsEnable()
    binding.etUsername.toggleIsEnable()
    binding.etFirstName.toggleIsEnable()
    binding.etLastName.toggleIsEnable()
    binding.etEmail.toggleIsEnable()
    binding.etPassword.toggleIsEnable()
    binding.signUpSecurityQuestion.toggleIsEnable()
    binding.securityAnswer.toggleIsEnable()
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
    } else if (userPass.length < 6) {
      binding.signupPassword.error = "password length must be atleast 6 !!!"
    } else {
      binding.signupEmail.error = null
      binding.signupPassword.error = null
      signUpViewModel.signUpRequest(
        NewUserDtoModel(
          userEmail, firstName, userName, lastName, userPass,
          SecurityQuestionDto(securityQuestionAnswer, "", securityQuestion!!)
        )
      )
    }
  }

  private fun doAfterTextChange() {
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

  override fun onSessionExpired() {
    // do nothing
  }
}
