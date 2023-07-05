package org.aossie.agoraandroid.ui.screens.createElection


import android.net.Uri
import android.os.Build.VERSION_CODES
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.domain.model.ElectionDtoModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PermissionsDialog
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryTitledSpinner
import org.aossie.agoraandroid.ui.screens.createElection.component.CandidateEditField
import org.aossie.agoraandroid.ui.screens.createElection.component.IconTextCheckBoxButton
import org.aossie.agoraandroid.ui.screens.createElection.component.SelectDateTimeField
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.AddCandidate
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.CreateElectionsClick
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.DeleteCandidate
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.EnteredElectionDescription
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.EnteredElectionName
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedBallotVisibility
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedEndDateTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedInvite
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedRealTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedStartDateTime
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedVoterListVisibility
import org.aossie.agoraandroid.ui.screens.createElection.events.CreateElectionScreenEvent.SelectedVotingAlgorithm
import org.aossie.agoraandroid.utilities.FileUtils

@RequiresApi(VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateElectionsScreen(
  electionDataState: ElectionDtoModel,
  progressErrorState: ScreensState,
  onEvent: (CreateElectionScreenEvent) -> Unit
) {

  val algorithmsList = stringArrayResource(id = R.array.algorithms).toList()
  val ballotVisibilityList = stringArrayResource(id = R.array.ballot_visibility).toList()
  val candidateText = remember {
    mutableStateOf("")
  }
  val storagePermissionState = rememberPermissionState(
    android.Manifest.permission.READ_EXTERNAL_STORAGE
  )
  val permissionDialog = remember { mutableStateOf(Pair("",false)) }
  val storagePermissionText = stringResource(id = string.storage_permission_required)
  val context = LocalContext.current

  val getCSVFile = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.GetContent(),
  ) { uri: Uri? ->
    uri?.let {
      FileUtils.getPathFromUri(context, it)?.let { path ->
        onEvent(CreateElectionScreenEvent.OpenCSVFileClick(path, context))
      }
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    Box(
      modifier = Modifier
        .padding(it)
    ){
      Column {
        LazyColumn(
          modifier = Modifier
            .fillMaxWidth()
            .weight(0.9f),
          contentPadding = PaddingValues(horizontal = 25.dp, vertical = 20.dp),
          verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          item {
            PrimaryLabelTextField(
              onValueChange = {
                onEvent(EnteredElectionName(it))
              },
              value = electionDataState.name ?: "",
              modifier = Modifier.fillMaxWidth(),
              label = stringResource(id = string.election_name)
            )
          }
          item {
            PrimaryLabelTextField(
              onValueChange = {
                onEvent(EnteredElectionDescription(it))
              },
              value = electionDataState.description ?: "",
              modifier = Modifier.fillMaxWidth(),
              label = stringResource(id = string.description)
            )
          }
          item {
            PrimaryTitledSpinner(
              title = stringResource(id = string.select_voting_algorithm),
              list = algorithmsList,
              selectedIndex = if (!electionDataState.votingAlgo.isNullOrEmpty()) algorithmsList.indexOf(
                electionDataState.votingAlgo!!
              ) else 0,
              onItemSelected = {
                onEvent(SelectedVotingAlgorithm(algorithmsList[it]))
              }
            )
          }
          item {
            CandidateEditField(
              text = candidateText.value,
              onTextChange = {
                candidateText.value = it
              },
              addCandidateClick = {
                onEvent(AddCandidate(it))
              },
              openCsvFileClick = {
                if(storagePermissionState.status.isGranted){
                  getCSVFile.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                }else{
                  if (storagePermissionState.status.shouldShowRationale) {
                    permissionDialog.value = Pair(storagePermissionText, true)
                  } else {
                    permissionDialog.value = Pair(storagePermissionText, true)
                  }
                }
              },
              candidatesList = electionDataState.candidates ?: emptyList(),
              deleteCandidateClick = {
                onEvent(DeleteCandidate(it))
              }
            )
          }
          item {
            PrimaryTitledSpinner(
              title = stringResource(id = string.ballot_visibility),
              list = ballotVisibilityList,
              selectedIndex = if (!electionDataState.ballotVisibility.isNullOrEmpty()) ballotVisibilityList.indexOf(
                electionDataState.ballotVisibility!!
              ) else 0,
              onItemSelected = {
                onEvent(SelectedBallotVisibility(ballotVisibilityList[it]))
              }
            )
          }
          item {
            IconTextCheckBoxButton(
              text = stringResource(id = string.voter_list_visibility),
              iconStart = drawable.ic_voter_list_visibility,
              checked = electionDataState.voterListVisibility ?: false,
              onCheckedChange = {
                onEvent(SelectedVoterListVisibility(it))
              })
          }
          item {
            Row(
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              SelectDateTimeField(
                modifier = Modifier.weight(0.5f),
                label = stringResource(id = R.string.start_date_format),
                dateTime = electionDataState.startingDate?: ""
              ) {
                onEvent(SelectedStartDateTime(it))
              }
              SelectDateTimeField(
                modifier = Modifier.weight(0.5f),
                label = stringResource(id = R.string.end_date_format),
                dateTime = electionDataState.endingDate?: ""
              ) {
                onEvent(SelectedEndDateTime(it))
              }
            }
          }
          item {
            IconTextCheckBoxButton(
              text = stringResource(id = string.real_time),
              iconStart = drawable.ic_real_time,
              checked = electionDataState.isRealTime ?: false,
              onCheckedChange = {
                onEvent(SelectedRealTime(it))
              }
            )
          }
          item {
            IconTextCheckBoxButton(
              text = stringResource(id = string.invite),
              iconStart = drawable.ic_invite_voters,
              checked = electionDataState.isInvite ?: false,
              onCheckedChange = {
                onEvent(SelectedInvite(it))
              }
            )
          }
          item {
            PrimaryButton(text = stringResource(id = string.Create_Election)) {
              onEvent(CreateElectionsClick)
            }
          }
        }
      }
      if (permissionDialog.value.second) {
        PermissionsDialog(
          title = "Permission !",
          description = permissionDialog.value.first,
          onDialogConfirm = {
            permissionDialog.value = Pair("",false)
            storagePermissionState.launchPermissionRequest()
          },
          onDialogDismiss = {
            permissionDialog.value = Pair("",false)
          }
        )
      }
      PrimaryProgressSnackView(screenState = progressErrorState)
    }
  }
}
