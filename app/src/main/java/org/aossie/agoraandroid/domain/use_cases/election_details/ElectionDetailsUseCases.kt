package org.aossie.agoraandroid.domain.use_cases.election_details

data class ElectionDetailsUseCases(
  val deleteElectionUseCase: DeleteElectionUseCase,
  val getBallotsUseCase: GetBallotsUseCase,
  val getElectionByIdUseCase: GetElectionByIdUseCase,
  val getResultUseCase: GetResultUseCase,
  val getVotersUseCase: GetVotersUseCase
)
