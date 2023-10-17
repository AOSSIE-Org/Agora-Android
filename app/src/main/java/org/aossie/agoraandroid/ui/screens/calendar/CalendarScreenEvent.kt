package org.aossie.agoraandroid.ui.screens.calendar

import java.time.LocalDate

sealed class CalendarScreenEvent {
  data class DateSelected(val date: LocalDate):CalendarScreenEvent()
  data class OnElectionClicked(val id: String):CalendarScreenEvent()
  object OnViewAllElectionClick:CalendarScreenEvent()
}
