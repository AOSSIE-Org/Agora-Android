package org.aossie.agoraandroid.ui.fragments.elections

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.linkedin.android.tachyon.DayView
import com.linkedin.android.tachyon.DayView.EventTimeRange
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendar.Builder
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.utilities.Coroutines
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Collections
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CalendarViewElectionFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

  lateinit var rootView: View

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  private var horizontalCalendar: HorizontalCalendar? = null

  private var day: Calendar? = null
  private var allEvents: HashMap<Long, List<Event>>? =
    null
  private var dateFormat: java.text.DateFormat? = null
  private var timeFormat: java.text.DateFormat? = null

  private var content: ViewGroup? = null
  private var dateTextView: TextView? = null
  private var scrollView: NestedScrollView? = null
  private var dayView: DayView? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_calendar_view_election, container, false)

    // Create a new calendar object set to the start of today
    day = Calendar.getInstance()
    day?.set(Calendar.HOUR_OF_DAY, 0)
    day?.set(Calendar.MINUTE, 0)
    day?.set(Calendar.SECOND, 0)
    day?.set(Calendar.MILLISECOND, 0)

    // Populate today's entry in the map with a list of example events
    allEvents = HashMap()

    dateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    timeFormat = java.text.DateFormat.getTimeInstance(
        java.text.DateFormat.SHORT, Locale.getDefault()
    )

    content = rootView.findViewById(R.id.sample_content)
    dateTextView = rootView.findViewById(R.id.sample_date)
    scrollView = rootView.findViewById(R.id.sample_scroll)
    dayView = rootView.findViewById(R.id.sample_day)

    // Inflate a label view for each hour the day view will display
    val hour = day!!.clone() as Calendar
    val hourLabelViews: MutableList<View> =
      ArrayList()
    for (i in dayView!!.startHour..dayView!!.endHour) {
      hour[Calendar.HOUR_OF_DAY] = i
      val hourLabelView =
        layoutInflater.inflate(layout.hour_label, dayView, false) as TextView
      hourLabelView.text = timeFormat!!.format(hour.time)
      hourLabelViews.add(hourLabelView)
    }
    dayView!!.setHourLabelViews(hourLabelViews)

    /* start 10 years ago from now */
    val startDate = Calendar.getInstance()
    startDate.add(Calendar.YEAR, -10)

    /* end after 10 years from now */
    val endDate = Calendar.getInstance()
    endDate.add(Calendar.YEAR, 10)

    // Default Date set to Today.
    val defaultSelectedDate = Calendar.getInstance()

    horizontalCalendar = Builder(rootView, R.id.calendarView)
        .range(startDate, endDate)
        .datesNumberOnScreen(5)
        .configure()
        .formatMiddleText("dd")
        .formatBottomText("EEE")
        .showTopText(false)
        .showBottomText(true)
        .textColor(Color.LTGRAY, Color.WHITE)
        .colorTextMiddle(
            Color.LTGRAY, Color.parseColor("#ffd54f")
        )
        .end()
        .defaultSelectedDate(defaultSelectedDate)
        .build()

    horizontalCalendar!!.calendarListener = object : HorizontalCalendarListener() {
      override fun onDateSelected(
        date: Calendar,
        position: Int
      ) {
        day = date
        onDayChange()
        scrollView!!.smoothScrollTo(0, dayView!!.firstEventTop)
      }
    }

    Coroutines.main {
      val elections = electionViewModel.elections.await()
      elections.observe(requireActivity(), Observer {
        if (it != null) {
          for (election in it) {
            addEvent(election)
            onDayChange()
          }
        }
      })
    }
    return rootView
  }

  private fun onDayChange() {
   dateTextView!!.text = dateFormat!!.format(day!!.time)
   if(activity != null) onEventsChange()
  }

  private fun onEventsChange() {
    // The day view needs a list of event views and a corresponding list of event time ranges
    var eventViews: MutableList<View?>? = null
    var eventTimeRanges: MutableList<EventTimeRange?>? = null
    val events = allEvents!![day!!.timeInMillis]
    val gson = Gson()
    Log.d("on Event Change", gson.toJson(events) + " , day time in millis : " + day!!.timeInMillis)
    if (events != null) {
      // Sort the events by start time so the layout happens in correct order
      Collections.sort(
          events
      ) { o1, o2 -> if (o1.hour < o2.hour) -1 else if (o1.hour == o2.hour) if (o1.minute < o2.minute) -1 else if (o1.minute == o2.minute) 0 else 1 else 1 }
      eventViews = ArrayList()
      eventTimeRanges = ArrayList()

      // Reclaim all of the existing event views so we can reuse them if needed, this process
      // can be useful if your day view is hosted in a recycler view for example
      val recycled = dayView!!.removeEventViews()
      var remaining = recycled?.size ?: 0
      for (event in events) {
        // Try to recycle an existing event view if there are enough left, otherwise inflate
        // a new one
        val eventView =
          if (remaining > 0) recycled!![--remaining] else layoutInflater.inflate(
              layout.event, dayView, false
          )
        (eventView.findViewById<View>(R.id.event_title) as TextView).text = event.title
        eventView.background = resources.getDrawable(event.color, resources.newTheme())

        // When an event is clicked
        eventView.setOnClickListener {
        }
        eventViews.add(eventView)

        // The day view needs the event time ranges in the start minute/end minute format,
        // so calculate those here
        val startMinute = 60 * event.hour + event.minute
        val endMinute = startMinute + event.duration
        eventTimeRanges.add(EventTimeRange(startMinute, endMinute.toInt()))
      }
    }

    // Update the day view with the new events
    dayView!!.setEventViews(eventViews, eventTimeRanges)
    scrollView!!.post(Runnable {
      scrollView!!.smoothScrollTo(0, dayView!!.firstEventTop-300)
    })
  }

  private fun addEvent(election: Election) {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    var formattedStartingDate: Date? = formatter.parse(election.start!!)
    val formattedEndingDate: Date? = formatter.parse(election.end!!)
    val currentDate = Calendar.getInstance()
        .time
    var eventColor = R.drawable.cornered_blue_background
    if (currentDate.before(formattedStartingDate)) {
      eventColor = R.drawable.cornered_green_background
    } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
      eventColor = R.drawable.cornered_red_background
    } else if (currentDate.after(formattedEndingDate)) {
      eventColor = R.drawable.cornered_blue_background
    }
    val startCalendar = Calendar.getInstance()
    startCalendar.time = formattedStartingDate!!
    val endCalendar = Calendar.getInstance()
    endCalendar.time = formattedEndingDate!!
    while (formattedStartingDate!!.before(formattedEndingDate)) {
      val sDate = startCalendar.clone() as Calendar
      sDate.set(Calendar.HOUR, 0)
      sDate.set(Calendar.MINUTE, 0)
      sDate.set(Calendar.SECOND, 0)
      sDate.set(Calendar.HOUR_OF_DAY, 0)
      var events: MutableList<Event>? = allEvents!![sDate.timeInMillis] as MutableList?
      if (events == null) {
        events = ArrayList()
      }
      val id = election._id
      val title = election.name
      val description = election.description
      val hour = startCalendar[Calendar.HOUR_OF_DAY]
      val minute = startCalendar[Calendar.MINUTE]
      val duration =
        (endCalendar.timeInMillis - startCalendar.timeInMillis) / 60000
      val color = eventColor
      events.add(Event(id, title, description, hour, minute, duration, color))
      allEvents!![sDate.timeInMillis] = events
      val gson = Gson()
      Log.d("all Events", gson.toJson(allEvents))
//      onDayChange()
      startCalendar.add(Calendar.DATE, 1)
      startCalendar.set(Calendar.HOUR, 0)
      startCalendar.set(Calendar.MINUTE, 0)
      startCalendar.set(Calendar.SECOND, 0)
      startCalendar.set(Calendar.HOUR_OF_DAY, 0)
      formattedStartingDate = startCalendar.time
    }
  }

  /**
   * A data class used to represent an event on the calendar.
   */
  private class Event(
    val id: String?,
    val title: String?,
    val description: String?,
    val hour: Int,
    val minute: Int,
    val duration: Long,
    @field:DrawableRes @param:DrawableRes val color: Int
  )
}