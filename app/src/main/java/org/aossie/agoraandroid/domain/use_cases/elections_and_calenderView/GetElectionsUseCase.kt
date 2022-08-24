package org.aossie.agoraandroid.domain.use_cases.elections_and_calenderView

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.mappers.ElectionEntityMapper
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  private val electionEntityMapper = ElectionEntityMapper()
  operator fun invoke(): LiveData<List<ElectionModel>> {
    return electionEntityMapper.mapFromEntityLiveDataList(
      electionsRepository.getElections()
    )
  }
}
