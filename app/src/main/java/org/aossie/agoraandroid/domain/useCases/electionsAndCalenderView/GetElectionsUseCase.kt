package org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView

import androidx.lifecycle.LiveData
import org.aossie.agoraandroid.data.mappers.Mappers
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class GetElectionsUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository,
  private val mappers: Mappers
) {
  operator fun invoke(): LiveData<List<ElectionModel>> {
    return mappers.electionEntityMapper.mapFromEntityLiveDataList(
      electionsRepository.getElections()
    )
  }
}
