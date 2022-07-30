package org.aossie.agoraandroid.domain.useCases.auth_useCases.login

import org.aossie.agoraandroid.data.db.entities.User
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
  private val repository: UserRepository,
  private val saveUserUseCase: SaveUserUseCase
) {
  var sessionExpiredListener: SessionExpiredListener? = null
  suspend operator fun invoke (
    trustedDevice: String? = null
  )  {
    try {
      val authResponse = repository.refreshAccessToken()
      authResponse.let {
        val user = User(
          it.username, it.email, it.firstName, it.lastName, it.avatarURL, it.crypto,
          it.twoFactorAuthentication,
          it.authToken?.token, it.authToken?.expiresOn, it.refreshToken?.token,
          it.refreshToken?.expiresOn, trustedDevice
        )
        saveUserUseCase(user)
      }
    } catch (e: Exception) {
      sessionExpiredListener?.onSessionExpired()
    }
  }
}