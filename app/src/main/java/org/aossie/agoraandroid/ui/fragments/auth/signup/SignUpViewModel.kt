package org.aossie.agoraandroid.ui.fragments.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.network.dto.SecurityQuestionDto
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import org.aossie.agoraandroid.domain.useCases.authentication.signUp.SignUpUseCase
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.auth.models.SignUpModel
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredEmail
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredFirstName
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredLastName
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredPassword
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredSecurityAnswer
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.EnteredUsername
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.SelectedSecurityQuestion
import org.aossie.agoraandroid.ui.screens.auth.signup.events.SignUpScreenEvent.SignUpClICK
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class SignUpViewModel
@Inject
constructor(
  private val signUpUseCase: SignUpUseCase
) : ViewModel() {

  private val _signUpDataState = MutableStateFlow (SignUpModel())
  val signUpDataState = _signUpDataState.asStateFlow()

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  lateinit var sessionExpiredListener: SessionExpiredListener
  private fun signUpRequest(
    userDataModel: NewUserDtoModel
  ) {
    showLoading("Creating account...")
    viewModelScope.launch {
      try {
        val call = signUpUseCase(userDataModel)
        Timber.d(call)
        showMessage(R.string.verify_account)
        hideLoading()
      } catch (e: ApiException) {
        hideLoading()
        if (e.message == "409") {
          showMessage(AppConstants.USER_ALREADY_FOUND_MESSAGE)
        } else {
          showMessage(e.message!!)
        }
      } catch (e: SessionExpirationException) {
        hideLoading()
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        hideLoading()
        showMessage(e.message!!)
      } catch (e: Exception) {
        hideLoading()
        showMessage(e.message!!)
      }
    }
  }

  fun onEvent(event: SignUpScreenEvent) {
    when(event){
      is EnteredPassword -> {
        _signUpDataState.value = signUpDataState.value.copy(
          password = event.password
        )
      }
      is EnteredUsername -> {
        _signUpDataState.value = signUpDataState.value.copy(
          username = event.username
        )
      }
      SignUpClICK -> {
        validateFields(_signUpDataState.value)
      }
      is EnteredEmail -> {
        _signUpDataState.value = signUpDataState.value.copy(
          email = event.email
        )
      }
      is EnteredFirstName -> {
        _signUpDataState.value = signUpDataState.value.copy(
          firstName = event.firstName
        )
      }
      is EnteredLastName -> {
        _signUpDataState.value = signUpDataState.value.copy(
          lastName = event.lastName
        )
      }
      is EnteredSecurityAnswer -> {
        _signUpDataState.value = signUpDataState.value.copy(
          securityAnswer = event.answer
        )
      }
      is SelectedSecurityQuestion -> {
        _signUpDataState.value = signUpDataState.value.copy(
          securityQuestion = event.question
        )
      }
      else -> {}
    }
  }

  private fun validateFields(value: SignUpModel) {
    if(value.username.trim().isEmpty()){
      showMessage(R.string.invalid_username)
    } else if (!Patterns.EMAIL_ADDRESS.matcher(value.email.trim())
        .matches()
    ) {
      showMessage("Enter a valid email address!!!")
    } else if (value.password.trim().length < 6) {
      showMessage("password length must be atleast 6 !!!")
    } else {
      signUpRequest(
        NewUserDtoModel(
          value.email.trim(), value.firstName.trim(), value.username.trim(), value.lastName.trim(), value.password.trim(),
          SecurityQuestionDto(value.securityAnswer.trim(), "", value.securityQuestion!!)
        )
      )
    }
  }

  private fun showLoading(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  fun showMessage(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair("",false)
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }
}
