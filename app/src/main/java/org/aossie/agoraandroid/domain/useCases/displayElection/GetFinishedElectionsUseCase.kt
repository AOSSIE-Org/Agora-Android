package org.aossie.agoraandroid.domain.useCases.displayElection

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetFinishedElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  operator fun invoke(
    date: String
  ): Flow<List<ElectionModel>> {
    val response = electionsRepository.getFinishedElections(date)
    return response.map { list -> list.map { mappers.electionEntityMapper.mapFromEntity(it) } }
  }
}
