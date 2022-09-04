package org.aossie.agoraandroid.domain.useCases.electionDetails

import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    id: String?
  ): List<WinnerDtoModel>? {
    return mappers.winnerDtoEntityMapper.mapFromEntityList(electionsRepository.getResult(id!!))
  }
}
