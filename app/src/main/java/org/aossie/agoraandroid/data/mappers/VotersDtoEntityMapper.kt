package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.domain.model.VotersDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class VotersDtoEntityMapper : EntityMapper<VotersDto, VotersDtoModel> {
  override fun mapFromEntity(entity: VotersDto): VotersDtoModel {
    return VotersDtoModel(
      voterName = entity.voterName,
      voterEmail = entity.voterEmail
    )
  }

  override fun mapToEntity(domainModel: VotersDtoModel): VotersDto {
    TODO("Not yet implemented")
  }

  fun mapFromEntityList(entityList: List<VotersDto>): List<VotersDtoModel> {
    return entityList.map { mapFromEntity(it) }
  }
}
