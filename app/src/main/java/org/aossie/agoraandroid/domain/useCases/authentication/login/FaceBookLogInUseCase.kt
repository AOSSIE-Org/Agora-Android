package org.aossie.agoraandroid.domain.useCases.authentication.login

import org.aossie.agoraandroid.data.mappers.AuthResponseEntityMapper
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.domain.repository.UserRepository
import javax.inject.Inject

class FaceBookLogInUseCase @Inject constructor(
  private val repository: UserRepository,
) {
  private val authResponseEntityMapper = AuthResponseEntityMapper()
  suspend operator fun invoke(): AuthResponseModel {
    return authResponseEntityMapper.mapFromEntity(repository.fbLogin())
  }
}
