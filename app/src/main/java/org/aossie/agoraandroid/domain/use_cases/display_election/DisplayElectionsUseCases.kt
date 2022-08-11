package org.aossie.agoraandroid.domain.use_cases.display_election

data class DisplayElectionsUseCases(
  val getActiveElectionsUseCase: GetActiveElectionsUseCase,
  val getFinishedElectionsUseCase: GetFinishedElectionsUseCase,
  val getPendingElectionsUseCase: GetPendingElectionsUseCase
)
