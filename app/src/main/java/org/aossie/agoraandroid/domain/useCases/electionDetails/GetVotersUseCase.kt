package org.aossie.agoraandroid.domain.useCases.electionDetails

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.VotersDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetVotersUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    id: String?
  ): List<VotersDtoModel> {
    return mappers.votersDtoEntityMapper.mapFromEntityList(electionsRepository.getVoters(id!!))
  }
}
