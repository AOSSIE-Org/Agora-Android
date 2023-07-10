package org.aossie.agoraandroid.ui.screens.inviteVoters

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.data.network.dto.VotersDto
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PermissionsDialog
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryButton
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryLabelTextField
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.AddVoterClick
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.EnteredVoterEmail
import org.aossie.agoraandroid.ui.screens.inviteVoters.InviteVotersScreenEvent.EnteredVoterName
import org.aossie.agoraandroid.utilities.FileUtils

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun InviteVotersScreen(
  progressErrorState: ScreensState,
  voterDataState: VotersDto,
  votersListState: List<VotersDto>,
  onEvent: (InviteVotersScreenEvent) -> Unit
){

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
        onEvent(InviteVotersScreenEvent.OpenCSVFileClick(path, context))
      }
    }
  }

  Scaffold() {
    Box(  modifier = Modifier
      .fillMaxSize()
      .padding(it)) {
      Column(
        modifier = Modifier
          .fillMaxSize()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .weight(0.85f)
            .padding(horizontal = 20.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Spacer(modifier = Modifier.height(10.dp))
          PrimaryLabelTextField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.name_of_the_voter),
            value = voterDataState.voterName!!,
            onValueChange = {
              onEvent(EnteredVoterName(it))
            }
          )
          PrimaryLabelTextField(
            modifier = Modifier.fillMaxWidth(),
            label = stringResource(id = string.email_of_voter),
            value = voterDataState.voterEmail!!,
            onValueChange = {
              onEvent(EnteredVoterEmail(it))
            },
            keyboardOptions = KeyboardOptions(
              keyboardType = KeyboardType.Email,
              imeAction = ImeAction.Done
            )
          )
          InviteVotersButton(
            addVoterClick = {
              onEvent(AddVoterClick)
            },
            openCSVFileClick = {
              if(storagePermissionState.status.isGranted){
                getCSVFile.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
              }else{
                if (storagePermissionState.status.shouldShowRationale) {
                  permissionDialog.value = Pair(storagePermissionText, true)
                } else {
                  permissionDialog.value = Pair(storagePermissionText, true)
                }
              }
            }
          )
          LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            itemsIndexed(votersListState) { index, item ->
              VotersItem(
                voterDto = item,
                onVoterDeleteClick = {
                  onEvent(InviteVotersScreenEvent.DeleteVoterClick(item))
                }
              )
            }
          }
        }
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .weight(0.15f)
            .padding(horizontal = 20.dp),
          contentAlignment = Alignment.Center
        ) {
          PrimaryButton(text = stringResource(id = string.invite_voter)) {
            onEvent(InviteVotersScreenEvent.InviteVotersClick)
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

@Composable
fun VotersItem(voterDto: VotersDto, onVoterDeleteClick: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(
      modifier = Modifier
        .weight(0.8f)
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline,
          shape = RoundedCornerShape(8.dp),
        )
        .padding(10.dp)
    ) {
      Text(text = voterDto.voterName!!, style = MaterialTheme.typography.titleLarge)
      Text(text = voterDto.voterEmail!!, style = MaterialTheme.typography.titleSmall)
    }
    Box(
      modifier = Modifier
        .size(42.dp)
        .background(
          color = MaterialTheme.colorScheme.errorContainer,
          shape = RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(12.dp))
        .clickable {
          onVoterDeleteClick.invoke()
        },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Rounded.Delete,
        contentDescription = "",
        tint = MaterialTheme.colorScheme.onErrorContainer
      )
    }
  }
}

@Composable
fun InviteVotersButton(
  addVoterClick: () -> Unit,
  openCSVFileClick : () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(modifier = Modifier.weight(0.8f)) {
      PrimaryButton(
        text = stringResource(id = string.add_voter),
        backgroundColor = MaterialTheme.colorScheme.background,
        strokeColor = MaterialTheme.colorScheme.inversePrimary,
        textColor = MaterialTheme.colorScheme.onBackground
      ) {
        addVoterClick.invoke()
      }
    }
    Box(
      modifier = Modifier
        .size(42.dp)
        .background(
          color = MaterialTheme.colorScheme.background,
          shape = RoundedCornerShape(10.dp)
        )
        .clip(RoundedCornerShape(12.dp))
        .border(
          width = 1.dp,
          color = Color(0xff34A853),
          shape = RoundedCornerShape(12.dp)
        )
        .clickable {
          openCSVFileClick.invoke()
        },
      contentAlignment = Alignment.Center
    ) {
      Icon(
        painter = painterResource(id = drawable.ic_csv_upload),
        contentDescription = "",
        tint = Color(0xff34A853)
      )
    }
  }
}
