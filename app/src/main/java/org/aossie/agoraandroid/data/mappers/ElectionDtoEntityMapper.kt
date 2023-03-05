package org.aossie.agoraandroid.data.mappers

import org.aossie.agoraandroid.data.network.dto.ElectionDto
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import javax.inject.Inject

class ElectionDtoEntityMapper @Inject constructor() {
  fun mapToEntity(domainModel: ElectionDtoModel): ElectionDto {
    return ElectionDto(
      ballot = domainModel.ballot,
      ballotVisibility = domainModel.ballotVisibility,
      candidates = domainModel.candidates,
      description = domainModel.description,
      electionType = domainModel.electionType,
      endingDate = domainModel.endingDate,
      isInvite = domainModel.isInvite,
      isRealTime = domainModel.isRealTime,
      name = domainModel.name,
      noVacancies = domainModel.noVacancies,
      startingDate = domainModel.startingDate,
      voterListVisibility = domainModel.voterListVisibility,
      votingAlgo = domainModel.votingAlgo,
      _id = domainModel._id
    )
  }

  fun mapFromEntity(entity: ElectionDto): ElectionDtoModel {
    return ElectionDtoModel(
      ballot = entity.ballot,
      ballotVisibility = entity.ballotVisibility,
      candidates = entity.candidates,
      description = entity.description,
      electionType = entity.electionType,
      endingDate = entity.endingDate,
      isInvite = entity.isInvite,
      isRealTime = entity.isRealTime,
      name = entity.name,
      noVacancies = entity.noVacancies,
      startingDate = entity.startingDate,
      voterListVisibility = entity.voterListVisibility,
      votingAlgo = entity.votingAlgo,
      _id = entity._id
    )
  }
}
