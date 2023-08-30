package org.aossie.agoraandroid.ui.screens.calendar.component

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.aossie.agoraandroid.ui.screens.calendar.LIMIT
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@RequiresApi(VERSION_CODES.O)
@Composable
fun HorizontalWeeksPager(
  refreshCalendar: Boolean,
  currentDate: LocalDate,
  onDateSelected: (LocalDate) -> Unit
) {
  val weeks = remember {
    mutableStateOf(getWeeks(currentDate, LIMIT))
  }
  val pagerState = rememberPagerState()

  LaunchedEffect(key1 = refreshCalendar) {
    val index = getCurrentWeekIndex(LocalDate.now(),weeks.value)
    if(currentDate != LocalDate.now()){
      onDateSelected(LocalDate.now())
    }else if(index != pagerState.currentPage){
      pagerState.animateScrollToPage(index)
    }
  }

  LaunchedEffect(key1 = currentDate) {
    var index = getCurrentWeekIndex(currentDate,weeks.value)
    if(index == -1){
      weeks.value = getWeeks(currentDate, LIMIT)
      index = getCurrentWeekIndex(currentDate,weeks.value)
    }
    pagerState.animateScrollToPage(index)
  }

  HorizontalPager(
    state = pagerState,
    pageCount = weeks.value.size
  ) { index ->
    Row(
      modifier = Modifier
        .fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      weeks.value[index].forEach {
        DayItem(
          date = it,
          isSelected = it == currentDate
        ) {
          onDateSelected(it)
        }
      }
    }
  }
}

@RequiresApi(VERSION_CODES.O)
fun getMonthAndYear(date: LocalDate): String {
  val format = DateTimeFormatter.ofPattern("MMMM yyyy")
  return format.format(date)
}

@RequiresApi(VERSION_CODES.O)
fun getCurrentWeekIndex(currentDate: LocalDate,weeks: List<List<LocalDate>>): Int {
  for ((index, week) in weeks.withIndex()) {
    if (currentDate in week) {
      return index
    }
  }
  return -1
}

@RequiresApi(VERSION_CODES.O)
@Composable
fun DayItem(date: LocalDate, isSelected: Boolean, onDaySelected: (LocalDate) -> Unit) {
  var colorContainer = if(isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background
  var colorContent = if(isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground
  if(date == LocalDate.now() && !isSelected){
    colorContainer = MaterialTheme.colorScheme.tertiaryContainer
    colorContent = MaterialTheme.colorScheme.onTertiaryContainer
  }
  Column(
    modifier = Modifier
      .clip(
        RoundedCornerShape(12.dp)
      )
      .background(
        color = colorContainer,
        shape = RoundedCornerShape(12.dp)
      )
      .clickable {
        onDaySelected(date)
      }
      .widthIn(min = 40.dp)
      .padding(10.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
  ) {
    Text(
      text = "${getDayOfWeek(date)}",
      color = colorContent,
      style = MaterialTheme.typography.titleSmall
    )
    Text(
      text = "${date.dayOfMonth}",
      color = colorContent,
      style = MaterialTheme.typography.titleLarge
    )
  }
}

@RequiresApi(VERSION_CODES.O)
fun getDayOfWeek(date: LocalDate): String {
  val format = DateTimeFormatter.ofPattern("EEE")
  return format.format(date)
}

@RequiresApi(VERSION_CODES.O)
fun getWeeks(currentDate: LocalDate, limit: Int): List<List<LocalDate>> {
  val weeks: MutableList<List<LocalDate>> = mutableListOf()
  var startDate = currentDate
  for (i in 0 until limit) {
    val week = getWeekDates(startDate)
    weeks.add(0, week)
    startDate = startDate.minusWeeks(1)
  }
  startDate = currentDate.plusWeeks(1)
  for (i in 0 until limit) {
    val week = getWeekDates(startDate)
    weeks.add(week)
    startDate = startDate.plusWeeks(1)
  }
  return weeks
}

@RequiresApi(VERSION_CODES.O)
fun getWeekDates(startDate: LocalDate): List<LocalDate> {
  val weekDates: MutableList<LocalDate> = mutableListOf()
  var date = startDate
  while (date.dayOfWeek != DayOfWeek.MONDAY) {
    date = date.minusDays(1)
  }
  for (i in 0 until 7) {
    weekDates.add(date)
    date = date.plusDays(1)
  }
  return weekDates
}