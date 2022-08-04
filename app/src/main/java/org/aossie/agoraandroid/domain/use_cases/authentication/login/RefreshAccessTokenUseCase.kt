package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  var sessionExpiredListener: SessionExpiredListener? = null
  suspend operator fun invoke(
  ) : AuthResponse {
    return repository.refreshAccessToken()
  }
}