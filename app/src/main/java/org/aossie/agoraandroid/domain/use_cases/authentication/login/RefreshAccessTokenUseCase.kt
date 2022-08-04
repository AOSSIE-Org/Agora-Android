package org.aossie.agoraandroid.domain.use_cases.authentication.login

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  var sessionExpiredListener: SessionExpiredListener? = null
  private val authResponseEntityMapper = AuthResponseEntityMapper()

  suspend operator fun invoke(
  ): AuthResponseModel {
    return authResponseEntityMapper.mapFromEntity(repository.refreshAccessToken())
  }
}
