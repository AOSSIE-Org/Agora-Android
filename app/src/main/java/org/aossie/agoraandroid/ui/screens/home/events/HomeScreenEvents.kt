package org.aossie.agoraandroid.ui.screens.home.events

sealed class HomeScreenEvents{
  object ActiveElectionClick:HomeScreenEvents()
  object TotalElectionClick:HomeScreenEvents()
  object PendingElectionClick:HomeScreenEvents()
  object FinishedElectionClick:HomeScreenEvents()
  object CreateElectionClick:HomeScreenEvents()
  object Refresh:HomeScreenEvents()
}
