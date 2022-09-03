package org.aossie.agoraandroid.domain.useCases.electionDetails

import org.aossie.agoraandroid.data.mappers.VotersDtoEntityMapper
import org.aossie.agoraandroid.domain.model.VotersDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetVotersUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val votersDtoEntityMapper = VotersDtoEntityMapper()
  suspend operator fun invoke(
    id: String?
  ): List<VotersDtoModel> {
    return votersDtoEntityMapper.mapFromEntityList(electionsRepository.getVoters(id!!))
  }
}
