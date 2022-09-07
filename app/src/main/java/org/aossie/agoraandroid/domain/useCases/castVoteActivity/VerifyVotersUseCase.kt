package org.aossie.agoraandroid.domain.useCases.castVoteActivity

import org.aossie.agoraandroid.data.mappers.ElectionDtoEntityMapper
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class VerifyVotersUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val electionDtoEntityMapper = ElectionDtoEntityMapper()
  suspend operator fun invoke(
    id: String
  ): ElectionDtoModel {
    return electionDtoEntityMapper.mapFromEntity(electionsRepository.verifyVoter(id))
  }
}
