package org.aossie.agoraandroid.ui.screens.createElection.component

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarSelection.Date
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection.HoursMinutes
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectDateTimeField(
  modifier: Modifier,
  label: String,
  dateTime: String,
  onDateTimeSelected: (String) -> Unit
) {

  val calendarState = rememberSheetState()
  val clockState = rememberSheetState()
  val selectedDate = remember {
    mutableStateOf<LocalDate?>(null)
  }
  CalendarDialog(
    state = calendarState,
    selection= Date { date ->
      selectedDate.value = date
      clockState.show()
    }
  )
  ClockDialog(
    state = clockState,
    selection = HoursMinutes { hours, minutes ->
      val date = selectedDate.value
      date?.let {
        val time = LocalDateTime.of(date, LocalTime.of(hours, minutes, 0))
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val dateTime = time.format(formatter)
        onDateTimeSelected(dateTime)
      }
    })

  Column(
    modifier = modifier
  ) {
    Text(
      text = label,
      style = MaterialTheme.typography.titleMedium,
    )
    Spacer(modifier = Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(TextFieldDefaults.MinHeight)
        .clip(RoundedCornerShape(6.dp))
        .border(
          width = 1.dp,
          color = MaterialTheme.colorScheme.outline,
          shape = RoundedCornerShape(6.dp)
        )
        .clickable {
          calendarState.show()
        }
        .padding(10.dp)
      ,
      contentAlignment = Alignment.CenterStart
    ) {
      Text(text = if(dateTime.isEmpty()) "Select Date-Time" else getDateTime(dateTime))
    }
  }
}


@RequiresApi(VERSION_CODES.O)
fun getDateTime(dateTime: String): String {
  val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
  val outputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm")

  val dateTime = LocalDateTime.parse(dateTime, inputFormatter)
  return dateTime.format(outputFormatter)
}