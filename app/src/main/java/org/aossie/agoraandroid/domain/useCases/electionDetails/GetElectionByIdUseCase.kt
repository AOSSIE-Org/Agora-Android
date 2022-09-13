package org.aossie.agoraandroid.domain.useCases.electionDetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetElectionByIdUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  operator fun invoke(
    id: String
  ): Flow<ElectionModel> {
    val response = electionsRepository.getElectionById(id)
    return response.map { mappers.electionEntityMapper.mapFromEntity(it) }
  }
}
