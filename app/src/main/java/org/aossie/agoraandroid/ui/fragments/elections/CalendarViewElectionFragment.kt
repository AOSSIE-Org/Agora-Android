package org.aossie.agoraandroid.ui.fragments.elections

import android.graphics.Color
import android.os.Bundle
import timber.log.Timber
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
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.linkedin.android.tachyon.DayView
import com.linkedin.android.tachyon.DayView.EventTimeRange
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendar.Builder
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.content_calendar_view.view.swipe_refresh
import kotlinx.android.synthetic.main.fragment_calendar_view_election.view.calendarView
import kotlinx.android.synthetic.main.fragment_calendar_view_election.view.fab_list_view
import kotlinx.android.synthetic.main.fragment_calendar_view_election.view.img_btn_month
import kotlinx.android.synthetic.main.fragment_calendar_view_election.view.progress_bar
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.hide
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
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
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
    rootView = inflater.inflate(layout.fragment_calendar_view_election, container, false)
    rootView.swipe_refresh.setColorSchemeResources(color.logo_yellow, color.logo_green)

    rootView.calendarView.visibility = View.GONE
    rootView.img_btn_month.setOnClickListener {
      if(rootView.calendarView.visibility == View.GONE) {
        rootView.calendarView.visibility = View.VISIBLE
        it.animate().rotation(180F).start()
      }
      else if(rootView.calendarView.visibility == View.VISIBLE) {
        rootView.calendarView.visibility = View.GONE
        it.animate().rotation(360F).start()
      }
    }

    day = Calendar.getInstance()
    day?.set(Calendar.HOUR_OF_DAY, 0)
    day?.set(Calendar.MINUTE, 0)
    day?.set(Calendar.SECOND, 0)
    day?.set(Calendar.MILLISECOND, 0)

    allEvents = HashMap()

    dateFormat = SimpleDateFormat("MMM yyyy", Locale.ENGLISH)
    timeFormat = java.text.DateFormat.getTimeInstance(
        java.text.DateFormat.SHORT, Locale.getDefault()
    )

    content = rootView.findViewById(R.id.sample_content)
    dateTextView = rootView.findViewById(R.id.sample_date)
    scrollView = rootView.findViewById(R.id.sample_scroll)
    dayView = rootView.findViewById(R.id.sample_day)

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
    dateTextView!!.text = dateFormat!!.format(day!!.time)

    val startDate = Calendar.getInstance()
    startDate.add(Calendar.YEAR, -10)

    val endDate = Calendar.getInstance()
    endDate.add(Calendar.YEAR, 10)

    val defaultSelectedDate = Calendar.getInstance()

    horizontalCalendar = Builder(rootView, R.id.horizontal_calendarView)
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
        rootView.calendarView.date = date.timeInMillis
        scrollView!!.smoothScrollTo(0, dayView!!.firstEventTop)
      }
    }

    rootView.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
      val cal = Calendar.getInstance()
      cal.set(Calendar.YEAR, year)
      cal.set(Calendar.MONTH, month)
      cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
      dateTextView!!.text = dateFormat!!.format(cal.time)
      horizontalCalendar!!.selectDate(cal, false)
    }

    rootView.swipe_refresh.setOnRefreshListener { doYourUpdate() }

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

    rootView.fab_list_view.setOnClickListener {
      Navigation
          .findNavController(rootView)
          .navigate(
              CalendarViewElectionFragmentDirections
                  .actionCalendarViewElectionFragmentToElectionsFragment()
          )
    }

    return rootView
  }

  override fun onDestroyView() {
    rootView.swipe_refresh.setOnRefreshListener(null)

    super.onDestroyView()
  }

  private fun doYourUpdate() {
    prefs.setUpdateNeeded(true)
    Navigation.findNavController(rootView)
        .navigate(R.id.calendarViewElectionFragment)
  }

  private fun onDayChange() {
   dateTextView!!.text = dateFormat!!.format(day!!.time)
   if(activity != null){
     onEventsChange()
   }
  }

  private fun onEventsChange() {
    var eventViews: MutableList<View?>? = null
    var eventTimeRanges: MutableList<EventTimeRange?>? = null
    val events = allEvents!![day!!.timeInMillis]
    val gson = Gson()
    Timber.tag("on Event Change").d(gson.toJson(events) + " , day time in millis : " + day!!.timeInMillis)
    if (events != null) {
      Collections.sort(
          events
      ) { o1, o2 -> if (o1.hour < o2.hour) -1 else if (o1.hour == o2.hour) if (o1.minute < o2.minute) -1 else if (o1.minute == o2.minute) 0 else 1 else 1 }
      eventViews = ArrayList()
      eventTimeRanges = ArrayList()

      val recycled = dayView!!.removeEventViews()
      var remaining = recycled?.size ?: 0
      for (event in events) {
        val eventView =
          if (remaining > 0) recycled!![--remaining] else layoutInflater.inflate(
              layout.list_item_event, dayView, false
          )
        val status = "Status : ${event.status}"
        (eventView.findViewById<View>(R.id.tv_event_title) as TextView).text = event.title
        (eventView.findViewById<View>(R.id.tv_event_description) as TextView).text = event.description
        (eventView.findViewById<View>(R.id.tv_event_status) as TextView).text = status
        eventView.background = resources.getDrawable(event.color, resources.newTheme())

        eventView.setOnClickListener {
          val action =
            CalendarViewElectionFragmentDirections
                .actionCalendarViewElectionFragmentToElectionDetailsFragment(event.id)
          Navigation.findNavController(rootView)
              .navigate(action)
        }

        eventViews.add(eventView)
        val startMinute = 60 * event.hour + event.minute
        val endMinute = startMinute + event.duration
        eventTimeRanges.add(EventTimeRange(startMinute, endMinute.toInt()))
      }
    }
    dayView!!.setEventViews(eventViews, eventTimeRanges)
    scrollView!!.post {
      scrollView!!.smoothScrollTo(0, dayView!!.firstEventTop-300)
    }
    if(rootView.progress_bar.visibility == View.VISIBLE){
      rootView.progress_bar.hide()
    }
  }

  private fun addEvent(election: Election) {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    var formattedStartingDate: Date? = formatter.parse(election.start!!)
    val formattedEndingDate: Date? = formatter.parse(election.end!!)
    val currentDate = Calendar.getInstance()
        .time
    var status : String ?= null
    var eventColor = R.drawable.cornered_blue_background
    if (currentDate.before(formattedStartingDate)) {
      eventColor = R.drawable.cornered_green_background
      status = "PENDING"
    } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
      eventColor = R.drawable.cornered_red_background
      status = "ACTIVE"
    } else if (currentDate.after(formattedEndingDate)) {
      eventColor = R.drawable.cornered_blue_background
      status = "FINISHED"
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
      events.add(Event(id, title, description, status, hour, minute, duration, eventColor))
      allEvents!![sDate.timeInMillis] = events
      val gson = Gson()
      Timber.tag("all Events").d(gson.toJson(allEvents))
      startCalendar.add(Calendar.DATE, 1)
      startCalendar.set(Calendar.HOUR, 0)
      startCalendar.set(Calendar.MINUTE, 0)
      startCalendar.set(Calendar.SECOND, 0)
      startCalendar.set(Calendar.HOUR_OF_DAY, 0)
      formattedStartingDate = startCalendar.time
    }
  }

  private class Event(
    val id: String,
    val title: String?,
    val description: String?,
    val status: String?,
    val hour: Int,
    val minute: Int,
    val duration: Long,
    @field:DrawableRes @param:DrawableRes val color: Int
  )
}