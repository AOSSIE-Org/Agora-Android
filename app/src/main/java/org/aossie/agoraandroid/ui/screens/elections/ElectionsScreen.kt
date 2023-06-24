package org.aossie.agoraandroid.ui.screens.elections

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryElectionSearchBar
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryElectionsList
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView

@Composable
fun ElectionsScreen(
  screenState: ScreensState,
  elections: List<ElectionModel>,
  searchText: String,
  onSearch: (String) -> Unit,
  onItemClicked: (String) -> Unit
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
      PrimaryProgressSnackView(screenState = screenState)
    }
  }

}

