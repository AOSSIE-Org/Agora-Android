package org.aossie.agoraandroid.domain.useCases.electionDetails

data class ElectionDetailsUseCases(
  val deleteElectionUseCase: DeleteElectionUseCase,
  val getBallotsUseCase: GetBallotsUseCase,
  val getElectionByIdUseCase: GetElectionByIdUseCase,
  val getResultUseCase: GetResultUseCase,
  val getVotersUseCase: GetVotersUseCase
)
