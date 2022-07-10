package org.aossie.agoraandroid.ui.fragments.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.repository.UserRepositoryImpl
import org.aossie.agoraandroid.data.remote.dto.NewUserDto
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import timber.log.Timber
import javax.inject.Inject

class SignUpViewModel
@Inject
constructor(
  private val userRepository: UserRepositoryImpl
) : ViewModel() {

  lateinit var sessionExpiredListener: SessionExpiredListener
  private val _getSignUpLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getSignUpLiveData = _getSignUpLiveData
  fun signUpRequest(
    userData: NewUserDto
  ) {
    _getSignUpLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        val call = userRepository.userSignup(userData)
        Timber.d(call)
        _getSignUpLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        if (e.message == "409") {
          _getSignUpLiveData.value = ResponseUI.error(AppConstants.USER_ALREADY_FOUND_MESSAGE)
        } else {
          _getSignUpLiveData.value = ResponseUI.error(e.message)
        }
      } catch (e: SessionExpirationException) {
        sessionExpiredListener.onSessionExpired()
      } catch (e: NoInternetException) {
        _getSignUpLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getSignUpLiveData.value = ResponseUI.error(e.message)
      }
    }
  }
}
