package org.aossie.agoraandroid.ui.screens.calendar

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons.Rounded
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.ui.screens.calendar.component.HorizontalWeeksPager
import org.aossie.agoraandroid.ui.screens.calendar.component.getMonthAndYear
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryElectionsList
import org.aossie.agoraandroid.ui.screens.common.component.PrimaryProgressSnackView
import java.time.LocalDate

const val LIMIT = 1000

@RequiresApi(VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
  progressErrorState: ScreensState,
  selectedDate: LocalDate,
  elections: List<ElectionModel>,
  onEvent: (CalendarScreenEvent) -> Unit
) {

  val calendarState = rememberSheetState()
  val refreshCalendar = remember {
    mutableStateOf(false)
  }

  CalendarDialog(
    state = calendarState,
    selection= CalendarSelection.Date { date ->
      onEvent(CalendarScreenEvent.DateSelected(date))
    }
  )

  Scaffold(
    containerColor = MaterialTheme.colorScheme.background,
    contentColor = MaterialTheme.colorScheme.onBackground,
    floatingActionButton = {
      if(!progressErrorState.loading.second){
        FloatingActionButton(
          onClick = {
            onEvent(CalendarScreenEvent.OnViewAllElectionClick)
          }) {
          Icon(
            imageVector = Rounded.ReceiptLong,
            contentDescription = "List" )
        }
      }
    },
    floatingActionButtonPosition = FabPosition.End
  ) {
    Box(modifier = Modifier
      .padding(it)
      .fillMaxSize()){
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column {
            Text(
              text = getMonthAndYear(selectedDate),
              style = MaterialTheme.typography.headlineSmall
            )
            Text(
              text = "${elections.size} ${stringResource(id = R.string.elections)}" ,
              style = MaterialTheme.typography.titleMedium,
              color = MaterialTheme.colorScheme.inversePrimary
            )
          }
          Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp)
          ) {
            FloatingActionButton(
              modifier = Modifier.size(40.dp),
              shape = FloatingActionButtonDefaults.smallShape,
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
              onClick = {
                refreshCalendar.value = !refreshCalendar.value
              }) {
              Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Rounded.Refresh,
                contentDescription = "Calendar Icon")
            }
            FloatingActionButton(
              modifier = Modifier.size(40.dp),
              shape = FloatingActionButtonDefaults.smallShape,
              containerColor = MaterialTheme.colorScheme.primaryContainer,
              contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
              onClick = {
                calendarState.show()
              }) {
              Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Rounded.CalendarMonth,
                contentDescription = "Calendar Icon")
            }
          }
        }
        HorizontalWeeksPager(
          refreshCalendar = refreshCalendar.value,
          currentDate = selectedDate
        ) { date ->
          onEvent(CalendarScreenEvent.DateSelected(date))
        }
        PrimaryElectionsList(elections = elections, onItemClicked = {
          onEvent(CalendarScreenEvent.OnElectionClicked(it))
        })
      }
      PrimaryProgressSnackView(screenState = progressErrorState)
    }
  }
}
