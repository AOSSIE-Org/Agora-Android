package org.aossie.agoraandroid.domain.use_cases.election_details

import org.aossie.agoraandroid.data.mappers.BallotDtoEntityMapper
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetBallotsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val ballotDtoEntityMapper = BallotDtoEntityMapper()
  suspend operator fun invoke(
    id: String?
  ): List<BallotDtoModel> {
    return ballotDtoEntityMapper.mapFromEntityList(electionsRepository.getBallots(id!!).ballots)
  }
}
