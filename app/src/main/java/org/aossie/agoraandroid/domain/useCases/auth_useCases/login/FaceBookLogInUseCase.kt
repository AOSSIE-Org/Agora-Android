package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import androidx.lifecycle.MutableLiveData
import org.aossie.agoraandroid.common.utilities.ApiException
import org.aossie.agoraandroid.common.utilities.NoInternetException
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.SessionExpirationException
import org.aossie.agoraandroid.common.utilities.subscribeToFCM
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import timber.log.Timber
import javax.inject.Inject

class FaceBookLogInUseCase @Inject constructor(
  private val repository: UserRepository,
  private val getUserDataUseCase_: GetUserDataUseCase_,
  private val prefs: PreferenceProvider
) {
  private val _getLoginLiveData: MutableLiveData<ResponseUI<String>> = MutableLiveData()
  val getLoginLiveData = _getLoginLiveData

  var sessionExpiredListener: SessionExpiredListener? = null

  suspend operator fun invoke(){
    try {
      val authResponse = repository.fbLogin()
      getUserDataUseCase_(authResponse)
      authResponse.email?.let {
        prefs.setMailId(it)
        subscribeToFCM(it)
      }
      Timber.d(authResponse.toString())
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