package org.aossie.agoraandroid.domain.useCases.castVoteActivity

import org.aossie.agoraandroid.domain.repository.ElectionsRepository
import javax.inject.Inject

class CastVoteUseCase @Inject constructor(
  private val electionsRepository: ElectionsRepository
) {
  suspend operator fun invoke(
    id: String,
    ballotInput: String,
    passCode: String
  ): List<String> {
    return electionsRepository.castVote(id, ballotInput, passCode)
  }
}
