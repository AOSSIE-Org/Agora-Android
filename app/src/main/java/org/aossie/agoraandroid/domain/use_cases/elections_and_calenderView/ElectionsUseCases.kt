package org.aossie.agoraandroid.domain.use_cases.elections_and_calenderView

import org.aossie.agoraandroid.domain.use_cases.home_fragment.FetchAndSaveElectionUseCase

data class ElectionsUseCases(
  val fetchAndSaveElectionUseCase: FetchAndSaveElectionUseCase,
  val getElectionsUseCase: GetElectionsUseCase
)
