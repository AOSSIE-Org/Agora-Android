package org.aossie.agoraandroid.domain.useCases.homeFragment

data class HomeFragmentUseCases(
  val fetchAndSaveElection: FetchAndSaveElectionUseCase,
  val getActiveElectionsCount: GetActiveElectionsCountUseCase,
  val getFinishedElectionsCount: GetFinishedElectionsCountUseCase,
  val getPendingElectionsCount: GetPendingElectionsCountUseCase,
  val getTotalElectionsCount: GetTotalElectionsCountUseCase,
  val deleteUser: DeleteUserUseCase,
  val logOut: LogOutUseCase
)
