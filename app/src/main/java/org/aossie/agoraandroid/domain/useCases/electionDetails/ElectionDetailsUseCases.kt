package org.aossie.agoraandroid.domain.useCases.electionDetails

data class ElectionDetailsUseCases(
  val deleteElection: DeleteElectionUseCase,
  val getBallots: GetBallotsUseCase,
  val getElectionById: GetElectionByIdUseCase,
  val getResult: GetResultUseCase,
  val getVoters: GetVotersUseCase
)
