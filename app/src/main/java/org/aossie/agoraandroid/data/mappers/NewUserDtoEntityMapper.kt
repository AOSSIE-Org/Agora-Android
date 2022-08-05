package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.NewUserDto
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class NewUserDtoEntityMapper : EntityMapper<NewUserDto, NewUserDtoModel> {
  override fun mapFromEntity(entity: NewUserDto): NewUserDtoModel {
    TODO("Not yet implemented")
  }

  override fun mapToEntity(domainModel: NewUserDtoModel): NewUserDto {
    return NewUserDto(
      email = domainModel.email,
      firstName = domainModel.firstName,
      identifier = domainModel.identifier,
      lastName = domainModel.lastName,
      password = domainModel.password,
      securityQuestion = domainModel.securityQuestion
    )
  }
}