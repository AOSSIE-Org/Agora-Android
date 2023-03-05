package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.NewUserDto
import org.aossie.agoraandroid.domain.model.NewUserDtoModel
import javax.inject.Inject

class NewUserDtoEntityMapper @Inject constructor() {
  fun mapToEntity(domainModel: NewUserDtoModel): NewUserDto {
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
