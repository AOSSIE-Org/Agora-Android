package org.aossie.agoraandroid.domain.use_cases.home_fragment

import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class FetchAndSaveElectionUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  suspend operator fun invoke() {
    return electionsRepository.fetchAndSaveElections()
  }
}
