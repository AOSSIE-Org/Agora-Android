package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.WinnerDto
import org.aossie.agoraandroid.domain.model.WinnerDtoModel

class WinnerDtoEntityMapper {
  fun mapFromEntity(entity: WinnerDto): WinnerDtoModel {
    return WinnerDtoModel(
      candidate = entity.candidate,
      score = entity.score
    )
  }

  fun mapFromEntityList(entityList: List<WinnerDto>?): List<WinnerDtoModel>? {
    return entityList?.map { mapFromEntity(it) }
  }
}
