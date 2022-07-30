package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.AppConstants
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import org.aossie.agoraandroid.common.utilities.subscribeToFCM
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.dto.LoginDto
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import timber.log.Timber
import javax.inject.Inject

class UserLogInUseCase @Inject constructor(
  private val repository: UserRepository,
  private val saveUserUseCase: SaveUserUseCase,
  private val prefs: PreferenceProvider
) {
  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData

  suspend operator fun invoke(
    identifier: String,
    password: String,
    trustedDevice: String? = null
  ) {
    _getLoginLiveData.value = ResponseUI.loading()
    if (identifier.isEmpty() || password.isEmpty()) {
      _getLoginLiveData.value = ResponseUI.error(AppConstants.INVALID_CREDENTIALS_MESSAGE)
      return
    }

      try {
        val authResponse = repository.userLogin(LoginDto(identifier,password,trustedDevice))
        authResponse.let {
          val user = User(
            it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
            it.twoFactorAuthentication,
            it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
            it.refreshToken?.expiresOn, trustedDevice
          )
          saveUserUseCase(user)
          it.email?.let { mail ->
            prefs.setMailId(mail)
            subscribeToFCM(mail)
          }
          Timber.d(user.toString())
          if (!it.twoFactorAuthentication!!) {
            _getLoginLiveData.value = ResponseUI.success()
          } else {
            _getLoginLiveData.value = ResponseUI.success(user.crypto!!)
          }
        }
      } catch (e: ApiException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLoginLiveData.value = ResponseUI.error(e.message)
      }
    }

}

