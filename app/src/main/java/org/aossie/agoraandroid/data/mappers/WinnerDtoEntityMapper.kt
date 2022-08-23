package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class WinnerDtoEntityMapper : EntityMapper<WinnerDto, WinnerDtoModel> {
  override fun mapFromEntity(entity: WinnerDto): WinnerDtoModel {
    return WinnerDtoModel(
      candidate = entity.candidate,
      score = entity.score
    )
  }

  override fun mapToEntity(domainModel: WinnerDtoModel): WinnerDto {
    TODO("Not yet implemented")
  }

  fun mapFromEntityList(entityList: List<WinnerDto>?): List<WinnerDtoModel>? {
    return entityList?.map { mapFromEntity(it) }
  }
}
