package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.data.remote.models.AuthResponse
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import timber.log.Timber
import javax.inject.Inject

class GetUserDataUseCase_ @Inject constructor(
  private val saveUserUseCase: SaveUserUseCase,
  private val prefs: PreferenceProvider
) {
  var sessionExpiredListener: SessionExpiredListener? = null

  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData
  suspend operator fun invoke(
     authResponse : AuthResponse
   ) {
    try {
      val user = User(
        authResponse.username, authResponse.email, authResponse.firstName, authResponse.lastName,
        authResponse.avatarURL, authResponse.crypto, authResponse.twoFactorAuthentication,
        authResponse.authToken?.token, authResponse.authToken?.expiresOn,
        authResponse.refreshToken?.token, authResponse.refreshToken?.expiresOn,
        authResponse.trustedDevice
      )
      saveUserUseCase(user)
      Timber.d(authResponse.toString())
      prefs.setIsFacebookUser(true)
      _getLoginLiveData.value = ResponseUI.success()
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