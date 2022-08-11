package org.aossie.agoraandroid.domain.use_cases.display_election

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.mappers.ElectionEntityMapper
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetFinishedElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val electionEntityMapper = ElectionEntityMapper()
  suspend operator fun invoke(
    date: String
  ): LiveData<List<ElectionModel>> {
    return electionEntityMapper.mapFromEntityLiveDataList(
      electionsRepository.getFinishedElections(
        date
      )
    )
  }
}
