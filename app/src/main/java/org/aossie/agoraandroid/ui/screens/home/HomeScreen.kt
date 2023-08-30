package org.aossie.agoraandroid.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.NavigateNext
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.ui.fragments.home.FINISHED_ELECTION_COUNT
import org.aossie.agoraandroid.ui.fragments.home.PENDING_ELECTION_COUNT
import org.aossie.agoraandroid.ui.fragments.home.TOTAL_ELECTION_COUNT
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.CreateElectionClick

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
  homeScreenDataState: MutableMap<String, Int>?,
  screenState: ScreensState,
  onClickEvents: (HomeScreenEvents) -> Unit
) {

  var refreshing by remember { mutableStateOf(false) }

  LaunchedEffect(key1  = screenState.loading.second){
    refreshing = screenState.loading.second
  }

  val state = rememberPullRefreshState(refreshing, onRefresh = {
    onClickEvents(HomeScreenEvents.Refresh)
  })

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground,
    floatingActionButtonPosition = FabPosition.End,
    floatingActionButton = {
      if(!screenState.loading.second){
        ExtendedFloatingActionButton(
          text = {
            Text(text = stringResource(id = string.Create_Election))
          },
          icon = {
            Icon(imageVector = Rounded.Add, contentDescription = "")
          },
          onClick = {
            onClickEvents(CreateElectionClick)
          })
      }
    }
  ) {
    Box(modifier = Modifier
      .padding(it)
      .pullRefresh(state)){
      LazyColumn(
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
      ){
        homeScreenDataState?.toList()?.sortedBy {
          it.first
        }?.let {list ->
          itemsIndexed(list){index, item ->
            HomeElectionCard(
              item = item,
              onClick = onClickEvents
            )
          }
        }
      }
      PrimaryProgressSnackView(screenState = screenState)
      PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeElectionCard(item: Pair<String, Int>, onClick: (HomeScreenEvents) -> Unit) {
  var title: String
  var icon: Int
  val color = when(item.first){
    TOTAL_ELECTION_COUNT -> {
      title = stringResource(id = R.string.total_elections)
      icon = R.drawable.img_total_election
      Pair(
        MaterialTheme.colorScheme.secondaryContainer,
        MaterialTheme.colorScheme.onSecondaryContainer
      )
    }
    PENDING_ELECTION_COUNT -> {
      title = stringResource(id = R.string.pending_elections)
      icon = R.drawable.img_pending_election
      Pair(
        MaterialTheme.colorScheme.tertiaryContainer,
        MaterialTheme.colorScheme.onTertiaryContainer
      )
    }
    FINISHED_ELECTION_COUNT -> {
      title = stringResource(id = R.string.finished_elections)
      icon = R.drawable.img_finished_election
      Pair(
        MaterialTheme.colorScheme.surface,
        MaterialTheme.colorScheme.onSurface
      )
    }
    else -> {
      title = stringResource(id = R.string.active_elections)
      icon = R.drawable.img_active_election
      Pair(
        MaterialTheme.colorScheme.primaryContainer,
        MaterialTheme.colorScheme.onPrimaryContainer
      )
    }
  }

  Card(
    onClick = {
       when(item.first){
         TOTAL_ELECTION_COUNT -> {
           onClick(HomeScreenEvents.TotalElectionClick)
         }
         PENDING_ELECTION_COUNT -> {
           onClick(HomeScreenEvents.PendingElectionClick)
         }
         FINISHED_ELECTION_COUNT -> {
           onClick(HomeScreenEvents.FinishedElectionClick)
         }
         else -> {
           onClick(HomeScreenEvents.ActiveElectionClick)
         }
       }
    },
    modifier = Modifier
      .fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
      containerColor = color.first,
      contentColor = color.second
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 10.dp
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(25.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
          text = "${item.second} $title" ,
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = stringResource(id = R.string.view_details),
          style = MaterialTheme.typography.bodyLarge
        )
      }
      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
      ) {
        Image(
          painter = painterResource(id = icon),
          contentDescription = "",
          modifier = Modifier.size(80.dp)
        )
        Icon(imageVector = Rounded.NavigateNext, contentDescription = "")
      }
    }
  }
}