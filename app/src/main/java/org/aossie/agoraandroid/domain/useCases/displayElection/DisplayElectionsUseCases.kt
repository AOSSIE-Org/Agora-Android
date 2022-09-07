package org.aossie.agoraandroid.domain.useCases.displayElection

data class DisplayElectionsUseCases(
  val getActiveElections: GetActiveElectionsUseCase,
  val getFinishedElections: GetFinishedElectionsUseCase,
  val getPendingElections: GetPendingElectionsUseCase
)
