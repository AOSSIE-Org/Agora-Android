package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.LoginDto
import org.aossie.agoraandroid.domain.model.LoginDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class LogInDtoEntityMapper : EntityMapper<LoginDto, LoginDtoModel> {
  override fun mapFromEntity(entity: LoginDto): LoginDtoModel {
    TODO("Not yet implemented")
  }

  override fun mapToEntity(domainModel: LoginDtoModel): LoginDto {
    return LoginDto(
      identifier = domainModel.identifier,
      trustedDevice = domainModel.trustedDevice,
      password = domainModel.password
    )
  }
}
