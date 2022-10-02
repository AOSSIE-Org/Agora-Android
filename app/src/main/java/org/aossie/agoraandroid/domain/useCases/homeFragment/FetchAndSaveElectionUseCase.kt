package org.aossie.agoraandroid.domain.useCases.homeFragment

import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class FetchAndSaveElectionUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  suspend operator fun invoke() {
    return electionsRepository.fetchAndSaveElections()
  }
}
