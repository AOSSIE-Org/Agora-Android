package org.aossie.agoraandroid.domain.useCases.electionDetails

import androidx.lifecycle.LiveData
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
  ): LiveData<ElectionModel> {
    return mappers.electionEntityMapper.mapFromEntityLiveData(electionsRepository.getElectionById(id))
  }
}
