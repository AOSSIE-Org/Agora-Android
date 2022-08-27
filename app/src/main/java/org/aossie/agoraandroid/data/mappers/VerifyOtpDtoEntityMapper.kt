package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.VerifyOtpDto
import org.aossie.agoraandroid.domain.model.VerifyOtpDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class VerifyOtpDtoEntityMapper : EntityMapper<VerifyOtpDto, VerifyOtpDtoModel> {
  override fun mapFromEntity(entity: VerifyOtpDto): VerifyOtpDtoModel {
    TODO("Not yet implemented")
  }

  override fun mapToEntity(domainModel: VerifyOtpDtoModel): VerifyOtpDto {
    return VerifyOtpDto(
      crypto = domainModel.crypto,
      otp = domainModel.otp,
      isTrusted = domainModel.isTrusted
    )
  }
}
