package org.aossie.agoraandroid.domain.useCases.displayElection

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.mappers.ElectionEntityMapper
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetPendingElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val electionEntityMapper = ElectionEntityMapper()
  suspend operator fun invoke(
    date: String
  ): LiveData<List<ElectionModel>> {
    return electionEntityMapper.mapFromEntityLiveDataList(
      electionsRepository.getPendingElections(
        date
      )
    )
  }
}
