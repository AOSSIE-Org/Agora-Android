package org.aossie.agoraandroid.domain.useCases.createElection

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class CreateElectionUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    electionData: ElectionDtoModel
  ): List<String> {
    return electionsRepository.createElection(
      mappers.electionDtoEntityMapper.mapToEntity(
        electionData
      )
    )
  }
}
