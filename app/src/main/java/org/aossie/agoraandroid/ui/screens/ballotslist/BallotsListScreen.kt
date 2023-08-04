package org.aossie.agoraandroid.ui.screens.ballotslist

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BallotsListScreen(
  ballotsList: List<BallotDtoModel>,
  progressErrorState: ScreensState,
  ){
  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.background,
  ) {
    Box {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(it),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        items(ballotsList) {
          BallotItem(it)
        }
      }
      if(ballotsList.isEmpty() && !progressErrorState.loading.second){
        Text(
          text = stringResource(id = R.string.empty_ballots),
          modifier = Modifier
            .align(Alignment.Center)
        )
      }
      PrimaryProgressSnackView(screenState = progressErrorState)
    }
  }
}

@Composable
fun BallotItem(ballotDtoModel: BallotDtoModel) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .border(
        width = 1.dp,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.outline
      )
      .padding(10.dp)
  ) {
    Text(text = ballotDtoModel.voteBallot!!, style = MaterialTheme.typography.titleLarge)
    Text(text = ballotDtoModel.hash!!, style = MaterialTheme.typography.titleSmall)
  }
}