package org.aossie.agoraandroid.domain.useCases.authentication.login

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import javax.inject.Inject

class RefreshAccessTokenUseCase @Inject constructor(
  private val repository: UserRepository,
  private val mappers: Mappers
) {
  var sessionExpiredListener: SessionExpiredListener? = null
  suspend operator fun invoke(): AuthResponseModel {
    return mappers.authResponseEntityMapper.mapFromEntity(repository.refreshAccessToken())
  }
}
