package org.aossie.agoraandroid.domain.useCases.homeFragment

data class HomeFragmentUseCases(
  val fetchAndSaveElectionUseCase: FetchAndSaveElectionUseCase,
  val getActiveElectionsCountUseCase: GetActiveElectionsCountUseCase,
  val getFinishedElectionsCountUseCase: GetFinishedElectionsCountUseCase,
  val getPendingElectionsCountUseCase: GetPendingElectionsCountUseCase,
  val getTotalElectionsCountUseCase: GetTotalElectionsCountUseCase,
  val deleteUserUseCase: DeleteUserUseCase,
  val logOutUseCase: LogOutUseCase
)
