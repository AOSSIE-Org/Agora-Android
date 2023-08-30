package org.aossie.agoraandroid.ui.screens.electionDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue.HalfExpanded
import androidx.compose.material.ModalBottomSheetValue.Hidden
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressView
import org.aossie.agoraandroid.ui.screens.common.component.PrimarySnackBar
import org.aossie.agoraandroid.ui.screens.electionDetails.component.ElectionData
import org.aossie.agoraandroid.ui.screens.electionDetails.component.ElectionDetailsBottomSheet
import org.aossie.agoraandroid.ui.screens.electionDetails.events.ElectionDetailsScreenEvent

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ElectionDetailsScreen(
  screenState: ScreensState,
  electionDetails: ElectionModel?,
  onEvent: (ElectionDetailsScreenEvent) -> Unit
){

  val modalSheetState = rememberModalBottomSheetState(
    initialValue = Hidden,
    confirmValueChange = { it != HalfExpanded },
    skipHalfExpanded = false
  )
  val coroutineScope = rememberCoroutineScope()

  ModalBottomSheetLayout(
    sheetState = modalSheetState,
    sheetBackgroundColor = MaterialTheme.colorScheme.background,
    sheetContentColor = MaterialTheme.colorScheme.onBackground,
    sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    sheetContent = ElectionDetailsBottomSheet(onEvent = onEvent,screenState = screenState)
  ) {
    Box(
      modifier = Modifier.fillMaxSize()
    ) {
      electionDetails?.let {election ->
        LazyColumn {
          item {
            ElectionData(election)
          }
        }
      }
      PrimaryProgressSnackView(screenState = screenState)
      if(!screenState.loading.second || !screenState.message.second){
        FloatingActionButton(
          modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(end = 20.dp, bottom = 20.dp),
          onClick = {
            coroutineScope.launch {
              modalSheetState.show()
            }
          }) {
          Icon(
            painter = painterResource(id = R.drawable.ic_menu),
            contentDescription = "")
        }
      }
    }
  }
}