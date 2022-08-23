package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.BallotDto
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.utilities.EntityMapper

class BallotDtoEntityMapper : EntityMapper<BallotDto, BallotDtoModel> {
  override fun mapFromEntity(entity: BallotDto): BallotDtoModel {
    return BallotDtoModel(
      hash = entity.hash,
      voteBallot = entity.voteBallot
    )
  }

  override fun mapToEntity(domainModel: BallotDtoModel): BallotDto {
    TODO("Not yet implemented")
  }

  fun mapFromEntityList(ballotDtoList: List<BallotDto>): List<BallotDtoModel> {
    return ballotDtoList.map { mapFromEntity(it) }
  }
}
