package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.responses.AuthResponse
import org.aossie.agoraandroid.domain.model.AuthResponseModel
import org.aossie.agoraandroid.utilities.EntityMapper

class AuthResponseEntityMapper : EntityMapper<AuthResponse, AuthResponseModel> {
  override fun mapFromEntity(entity: AuthResponse): AuthResponseModel {
    return AuthResponseModel(
      username = entity.username,
      email = entity.email,
      firstName = entity.firstName,
      lastName = entity.lastName,
      avatarURL = entity.avatarURL,
      twoFactorAuthentication = entity.twoFactorAuthentication,
      crypto = entity.crypto,
      authToken = entity.authToken,
      refreshToken = entity.refreshToken,
      trustedDevice = entity.trustedDevice
    )
  }

  override fun mapToEntity(domainModel: AuthResponseModel): AuthResponse {
    TODO("Not yet implemented")
  }
}
