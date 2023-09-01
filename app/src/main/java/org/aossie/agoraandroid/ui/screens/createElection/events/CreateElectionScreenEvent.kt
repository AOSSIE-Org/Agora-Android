package org.aossie.agoraandroid.ui.screens.createElection.events

import android.content.Context

sealed class CreateElectionScreenEvent {
  data class EnteredElectionName(val electionName: String): CreateElectionScreenEvent()
  data class EnteredElectionDescription(val electionDescription: String): CreateElectionScreenEvent()
  data class SelectedVotingAlgorithm(val votingAlgorithm: String): CreateElectionScreenEvent()
  data class AddCandidate(val candidateName: String): CreateElectionScreenEvent()
  data class DeleteCandidate(val candidateName: String): CreateElectionScreenEvent()
  data class SelectedBallotVisibility(val ballotVisibility: String): CreateElectionScreenEvent()
  data class SelectedVoterListVisibility(val voterListVisibility: Boolean): CreateElectionScreenEvent()
  data class SelectedStartDateTime(val startDateTime: String): CreateElectionScreenEvent()
  data class SelectedEndDateTime(val endDateTime: String): CreateElectionScreenEvent()
  data class SelectedRealTime(val realTime:Boolean): CreateElectionScreenEvent()
  data class SelectedInvite(val invite:Boolean): CreateElectionScreenEvent()
  object SnackBarActionClick: CreateElectionScreenEvent()
  data class OpenCSVFileClick(val filePath: String, val context: Context): CreateElectionScreenEvent()
  object CreateElectionsClick: CreateElectionScreenEvent()
}
