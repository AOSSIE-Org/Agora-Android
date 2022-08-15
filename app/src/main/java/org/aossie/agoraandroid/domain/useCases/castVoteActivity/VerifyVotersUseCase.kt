package org.aossie.agoraandroid.domain.useCases.castVoteActivity

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class VerifyVotersUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    id: String
  ): ElectionDtoModel {
    return mappers.electionDtoEntityMapper.mapFromEntity(electionsRepository.verifyVoter(id))
  }
}
