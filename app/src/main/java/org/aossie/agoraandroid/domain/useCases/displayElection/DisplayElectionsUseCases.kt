package org.aossie.agoraandroid.domain.useCases.displayElection

data class DisplayElectionsUseCases(
  val getActiveElectionsUseCase: GetActiveElectionsUseCase,
  val getFinishedElectionsUseCase: GetFinishedElectionsUseCase,
  val getPendingElectionsUseCase: GetPendingElectionsUseCase
)
