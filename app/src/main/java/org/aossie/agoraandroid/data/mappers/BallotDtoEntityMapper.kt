package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.BallotDto
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import javax.inject.Inject

class BallotDtoEntityMapper @Inject constructor() {
  fun mapFromEntity(entity: BallotDto): BallotDtoModel {
    return BallotDtoModel(
      hash = entity.hash,
      voteBallot = entity.voteBallot
    )
  }

  fun mapFromEntityList(ballotDtoList: List<BallotDto>): List<BallotDtoModel> {
    return ballotDtoList.map { mapFromEntity(it) }
  }
}
