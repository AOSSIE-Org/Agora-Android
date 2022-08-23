package org.aossie.agoraandroid.domain.use_cases.create_election

import org.aossie.agoraandroid.data.mappers.ElectionDtoEntityMapper
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class CreateElectionUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val electionDtoEntityMapper = ElectionDtoEntityMapper()
  suspend operator fun invoke(
    electionData: ElectionDtoModel
  ): List<String> {
    return electionsRepository.createElection(electionDtoEntityMapper.mapToEntity(electionData))
  }
}
