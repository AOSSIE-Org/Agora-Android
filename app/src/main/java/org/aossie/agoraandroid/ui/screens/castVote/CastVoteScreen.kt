package org.aossie.agoraandroid.ui.screens.castVote

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.ui.screens.castVote.component.CandidatesData
import org.aossie.agoraandroid.ui.screens.castVote.component.CastVoteElectionData
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.AppConstants.Status.ACTIVE
import org.aossie.agoraandroid.utilities.AppConstants.Status.FINISHED
import org.aossie.agoraandroid.utilities.AppConstants.Status.PENDING

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CastVoteScreen(
  screenState: ScreensState,
  electionDetails: ElectionDtoModel?,
  onEvent: (CastVoteScreenEvent) -> Unit
) {

  val electionStatus = remember {
    mutableStateOf<AppConstants.Status?>(null)
  }
  val checkedCandidates = remember {
    mutableStateOf(emptyList<String>())
  }
  val castVoteDialogState = remember {
    mutableStateOf(false)
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(text = stringResource(id = R.string.cast_vote))
        },
        modifier = Modifier.shadow(
          elevation = 10.dp
        )
      )
    }
  ) {
    Box(Modifier.padding(it)) {
      electionDetails?.let {election ->
        Column {
          LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .weight(0.85f)) {
            item {
              CastVoteElectionData(election) {
                electionStatus.value = it
              }
            }
            item {
              CandidatesData(election.candidates,electionStatus.value,checkedCandidates)
            }
          }
          Box(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .weight(0.15f),
            contentAlignment = Alignment.Center
          ) {
            electionStatus.value?.let {
              when(it) {
                PENDING -> {
                  Text(
                    text = stringResource(id = R.string.election_not_started)
                  )
                }
                ACTIVE -> {
                  if(checkedCandidates.value.isNotEmpty()) {
                    PrimaryButton(
                      text = stringResource(id = string.cast_vote),
                      backgroundColor = MaterialTheme.colorScheme.primary,
                      textColor = MaterialTheme.colorScheme.onPrimary,
                      strokeColor = MaterialTheme.colorScheme.primary
                    ) {
                      castVoteDialogState.value = true
                    }
                  }
                }
                FINISHED -> {
                  Text(
                    text = stringResource(id = R.string.election_finished)
                  )
                }
              }
            }
          }
        }
      }

      if(castVoteDialogState.value) {
        AlertDialog(
          onDismissRequest = { /* Handle dialog dismiss */ },
          title = {
            Text(stringResource(id = R.string.confirm_vote))
          },
          text = {
            Text(stringResource(id = R.string.confirm_vote_message))
          },
          confirmButton = {
            Button(
              onClick = {
                castVoteDialogState.value = false
                onEvent(CastVoteScreenEvent.CastVoteClick(checkedCandidates.value))
              }
            ) {
              Text(stringResource(id = R.string.confirm_button))
            }
          },
          dismissButton = {
            Button(
              onClick = { castVoteDialogState.value = false }
            ) {
              Text(stringResource(id = R.string.cancel_button))
            }
          },
          properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
          )
        )
      }
      
      PrimaryProgressSnackView(screenState = screenState)
    }
  }
}