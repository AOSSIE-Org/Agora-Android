package org.aossie.agoraandroid.domain.use_cases.election_details

import org.aossie.agoraandroid.data.mappers.WinnerDtoEntityMapper
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val winnerDtoEntityMapper = WinnerDtoEntityMapper()
  suspend operator fun invoke(
    id: String?
  ): List<WinnerDtoModel>? {
    return winnerDtoEntityMapper.mapFromEntityList(electionsRepository.getResult(id!!))
  }
}
