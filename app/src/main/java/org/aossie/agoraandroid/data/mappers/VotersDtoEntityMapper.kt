package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.domain.model.VotersDtoModel

class VotersDtoEntityMapper {
  fun mapFromEntity(entity: VotersDto): VotersDtoModel {
    return VotersDtoModel(
      voterName = entity.voterName,
      voterEmail = entity.voterEmail
    )
  }

  fun mapFromEntityList(entityList: List<VotersDto>): List<VotersDtoModel> {
    return entityList.map { mapFromEntity(it) }
  }
}
