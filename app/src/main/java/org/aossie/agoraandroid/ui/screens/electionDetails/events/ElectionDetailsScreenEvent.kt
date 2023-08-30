package org.aossie.agoraandroid.ui.screens.electionDetails.events

sealed class ElectionDetailsScreenEvent{
  object DeleteElectionClick:ElectionDetailsScreenEvent()
  object InviteVotersClick:ElectionDetailsScreenEvent()
  object ViewVotersClick:ElectionDetailsScreenEvent()
  object BallotClick:ElectionDetailsScreenEvent()
  object ResultClick:ElectionDetailsScreenEvent()
}
