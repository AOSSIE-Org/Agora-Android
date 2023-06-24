package org.aossie.agoraandroid.ui.screens.elections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryElectionSearchBar
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryElectionsList
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressView
import org.aossie.agoraandroid.ui.screens.common.component.PrimarySnackBar

@Composable
fun ElectionsScreen(
  screenState: ScreensState,
  elections: List<ElectionModel>,
  searchText: String,
  onSearch: (String) -> Unit,
  onItemClicked: (String) -> Unit,
  onSnackActionClick: () -> Unit
) {
  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground
  ) {
    Box(modifier = Modifier.padding(it)){
      Column {
        PrimaryElectionSearchBar(
          searchText = searchText,
          onSearch = onSearch,
          content = {
            PrimaryElectionsList(elections, onItemClicked)
          }
        )
        PrimaryElectionsList(elections, onItemClicked)
      }

      Column(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(
            horizontal = 20.dp,
          ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        if (screenState.error.second) {
          PrimarySnackBar(
            text = screenState.error.first,
            actionText = "ok"
          ) {
            onSnackActionClick()
          }
        }
        if (screenState.errorResource.second) {
          PrimarySnackBar(
            text = stringResource(id = screenState.errorResource.first),
            actionText = "ok"
          ) {
            onSnackActionClick()
          }
        }
        if (screenState.isLoading.second) {
          PrimaryProgressView(
            text = screenState.isLoading.first,
          )
        }
      }
    }
  }

}

