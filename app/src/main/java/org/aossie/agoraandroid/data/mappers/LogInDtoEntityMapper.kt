package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.domain.model.LoginDtoModel

class LogInDtoEntityMapper {
  fun mapToEntity(domainModel: LoginDtoModel): LoginDto {
    return LoginDto(
      identifier = domainModel.identifier,
      trustedDevice = domainModel.trustedDevice,
      password = domainModel.password
    )
  }
}
