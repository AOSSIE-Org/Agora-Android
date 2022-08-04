package org.aossie.agoraandroid.ui.fragments.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.network.dto.NewUserDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class SignUpViewModel
@Inject
constructor(
<<<<<<< HEAD
  private val signUpUseCase: SignUpUseCase
=======
  private val userRepository: UserRepositoryImpl
>>>>>>> 0160a1b (added use cases for loginFragment)
) : ViewModel() {

  lateinit var sessionExpiredListener: SessionExpiredListener
  private val _getSignUpStateFlow: MutableStateFlow<ResponseUI<Any>?> = MutableStateFlow(null)
  val getSignUpStateFlow = _getSignUpStateFlow.asStateFlow()
  fun signUpRequest(
    userDataModel: NewUserDtoModel
  ) {
    _getSignUpStateFlow.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val call = signUpUseCase(userDataModel)
        Timber.d(call)
        _getSignUpStateFlow.value = ResponseUI.success()
      } catch (e: ApiException) {
        if (e.message == "409") {
          _getSignUpStateFlow.value = ResponseUI.error(AppConstants.USER_ALREADY_FOUND_MESSAGE)
        } else {
          _getSignUpStateFlow.value = ResponseUI.error(e.message)
        }
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getSignUpStateFlow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getSignUpStateFlow.value = ResponseUI.error(e.message)
      }
    }
  }
}
