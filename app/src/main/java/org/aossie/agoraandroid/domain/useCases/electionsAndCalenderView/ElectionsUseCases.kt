package org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView

import org.aossie.agoraandroid.domain.useCases.homeFragment.FetchAndSaveElectionUseCase

data class ElectionsUseCases(
  val fetchAndSaveElectionUseCase: FetchAndSaveElectionUseCase,
  val getElectionsUseCase: GetElectionsUseCase
)
