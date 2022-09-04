package org.aossie.agoraandroid.domain.useCases.displayElection

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetFinishedElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  suspend operator fun invoke(
    date: String
  ): LiveData<List<ElectionModel>> {
    return mappers.electionEntityMapper.mapFromEntityLiveDataList(
      electionsRepository.getFinishedElections(
        date
      )
    )
  }
}
