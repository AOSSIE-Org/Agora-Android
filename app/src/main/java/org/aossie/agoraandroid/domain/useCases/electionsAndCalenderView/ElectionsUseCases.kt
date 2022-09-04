package org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView

import org.aossie.agoraandroid.domain.useCases.homeFragment.FetchAndSaveElectionUseCase

data class ElectionsUseCases(
  val fetchAndSaveElection: FetchAndSaveElectionUseCase,
  val getElections: GetElectionsUseCase
)
