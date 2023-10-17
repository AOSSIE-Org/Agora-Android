package org.aossie.agoraandroid.ui.screens.result

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.domain.model.WinnerDtoModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PermissionsDialog
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.result.ResultScreenEvent.OnExportCSVClick
import org.aossie.agoraandroid.ui.screens.result.component.BarGraph
import org.aossie.agoraandroid.ui.screens.result.component.PieChart
import org.aossie.agoraandroid.ui.screens.result.component.WinnerCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ResultScreen(
  progressErrorState: ScreensState,
  winnerDto: WinnerDtoModel?,
  onEvent: (ResultScreenEvent) -> Unit
){

  var mDisplayMenu by remember { mutableStateOf(false) }
  val storagePermissionState = rememberPermissionState(
    android.Manifest.permission.READ_EXTERNAL_STORAGE
  )
  val permissionDialog = remember { mutableStateOf(Pair("",false)) }
  val storagePermissionText = stringResource(id = R.string.storage_permission_required)

  val screenshotState = rememberScreenshotState()

  LaunchedEffect(key1 = screenshotState.bitmapState.value){
    screenshotState.bitmapState.value?.let {
      onEvent(ResultScreenEvent.OnShareClick(it))
    }
  }
  var animateFloat by remember {
    mutableStateOf(0f)
  }
  var animateClose = animateFloatAsState(targetValue = animateFloat, label = "")

  Scaffold(
    floatingActionButton = {
      if(!progressErrorState.loading.second && !progressErrorState.message.second) {
        Column(
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          if (winnerDto?.score?.numerator != null && mDisplayMenu) {
            FloatingActionButton(
              onClick = {
                mDisplayMenu = !mDisplayMenu
                if (mDisplayMenu) {
                  animateFloat = 45f
                } else {
                  animateFloat = 0f
                }
                screenshotState.capture()
              }
            ) {
              Icon(painter = painterResource(id = drawable.ic_share_icon), contentDescription = "")
            }
            FloatingActionButton(
              onClick = {
                mDisplayMenu = !mDisplayMenu
                if (mDisplayMenu) {
                  animateFloat = 45f
                } else {
                  animateFloat = 0f
                }
                if (storagePermissionState.status.isGranted) {
                  onEvent(OnExportCSVClick)
                } else {
                  if (storagePermissionState.status.shouldShowRationale) {
                    permissionDialog.value = Pair(storagePermissionText, true)
                  } else {
                    permissionDialog.value = Pair(storagePermissionText, true)
                  }
                }
              }
            ) {
              Icon(painter = painterResource(id = drawable.ic_csv_upload), contentDescription = "")
            }
          }
          FloatingActionButton(
            onClick = {
              mDisplayMenu = !mDisplayMenu
              if (mDisplayMenu) {
                animateFloat = 45f
              } else {
                animateFloat = 0f
              }
            },
            containerColor = if (mDisplayMenu) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer,
            contentColor = if (mDisplayMenu) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onPrimaryContainer,
          ) {
            Icon(
              imageVector = Rounded.Add,
              contentDescription = "",
              modifier = Modifier
                .animateContentSize()
                .rotate(animateClose.value)
            )
          }
        }
      }
    }
  ) {
    Box(modifier = Modifier
      .fillMaxSize()
      .padding(it)) {
      winnerDto?.let {
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(20.dp),
          verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          item {
            ScreenshotBox(screenshotState = screenshotState) {
              Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                WinnerCard(winnerName = winnerDto.candidate?.name ?: "")
                BarGraph(winnerDto = winnerDto)
              }
            }
          }
          item {
            PieChart(winnerDto = winnerDto)
          }
        }
      } ?: run {
        Text(
          text = stringResource(id = R.string.nothing_to_show_here),
          modifier = Modifier.align(Alignment.Center)
        )
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
fun MenuItem(text: String, icon: Painter, onClick: () -> Unit) {
  DropdownMenuItem(
    text = {
      Text(text = text)
    },
    onClick = onClick,
    leadingIcon = {
      Icon(painter = icon, contentDescription = "")
    }
  )
}