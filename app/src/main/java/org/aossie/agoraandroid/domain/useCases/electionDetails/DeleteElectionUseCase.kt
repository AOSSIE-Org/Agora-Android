package org.aossie.agoraandroid.domain.useCases.electionDetails

import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class DeleteElectionUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  suspend operator fun invoke(
    id: String?
  ): List<String> {
    return electionsRepository.deleteElection(id!!)
  }
}
