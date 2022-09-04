package org.aossie.agoraandroid.domain.useCases.electionDetails

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetBallotsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    id: String?
  ): List<BallotDtoModel> {
    return mappers.ballotDtoEntityMapper.mapFromEntityList(electionsRepository.getBallots(id!!).ballots)
  }
}
