package org.aossie.agoraandroid.ui.fragments.elections

import android.R.color
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
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
import org.aossie.agoraandroid.R.string
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

  private val INITIAL_EVENTS: Array<Event> =
    arrayOf(
        Event(
            "Walk the dog", "Park", 0, 0, 1600, color.holo_red_dark
        ),
        Event(
            "Meeting", "Office", 1, 30, 90, color.holo_purple
        ),
        Event(
            "Phone call", "555-5555", 2, 0, 45, color.holo_orange_dark
        ),
        Event(
            "Lunch", "Cafeteria", 2, 30, 30, color.holo_green_dark
        ),
        Event(
            "Dinner", "Home", 18, 0, 30, color.holo_green_dark
        )
    )

  private var day: Calendar? = null
  private var allEvents: HashMap<Long, List<Event>>? =
    null
  private var dateFormat: java.text.DateFormat? = null
  private var timeFormat: java.text.DateFormat? = null
  private var editEventDate: Calendar? = null
  private var editEventStartTime: Calendar? = null
  private var editEventEndTime: Calendar? = null
  private var editEventDraft: Event? = null

  private var content: ViewGroup? = null
  private var dateTextView: TextView? = null
  private var scrollView: ScrollView? = null
  private var dayView: DayView? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_calendar_view_election, container, false)

    // Create a new calendar object set to the start of today

    // Create a new calendar object set to the start of today
    day = Calendar.getInstance()
    day?.set(Calendar.HOUR_OF_DAY, 0)
    day?.set(Calendar.MINUTE, 0)
    day?.set(Calendar.SECOND, 0)
    day?.set(Calendar.MILLISECOND, 0)

    // Populate today's entry in the map with a list of example events

    // Populate today's entry in the map with a list of example events
    allEvents = HashMap()
//    allEvents!!.put(
//        day!!.timeInMillis,
//        ArrayList(
//            listOf(
//                *INITIAL_EVENTS
//            )
//        )
//    )

    dateFormat = java.text.DateFormat.getDateInstance(
        java.text.DateFormat.MEDIUM, Locale.getDefault()
    )
    timeFormat = java.text.DateFormat.getTimeInstance(
        java.text.DateFormat.SHORT, Locale.getDefault()
    )

    content = rootView.findViewById(R.id.sample_content)
    dateTextView = rootView.findViewById(R.id.sample_date)
    scrollView = rootView.findViewById(R.id.sample_scroll)
    dayView = rootView.findViewById(R.id.sample_day)

    // Inflate a label view for each hour the day view will display

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

    onDayChange()

    /* start 2 months ago from now */

    /* start 2 months ago from now */
    val startDate = Calendar.getInstance()
    startDate.add(Calendar.YEAR, -10)

    /* end after 2 months from now */

    /* end after 2 months from now */
    val endDate = Calendar.getInstance()
    endDate.add(Calendar.YEAR, 10)

    // Default Date set to Today.

    // Default Date set to Today.
    val defaultSelectedDate = Calendar.getInstance()

    horizontalCalendar = Builder(rootView, R.id.calendarView)
        .range(startDate, endDate)
        .datesNumberOnScreen(5)
        .configure()
        .formatTopText("MMM")
        .formatMiddleText("dd")
        .formatBottomText("EEE")
        .showTopText(true)
        .showBottomText(true)
        .textColor(Color.LTGRAY, Color.WHITE)
        .colorTextMiddle(
            Color.LTGRAY, Color.parseColor("#ffd54f")
        )
        .end()
        .defaultSelectedDate(defaultSelectedDate)
        .build()

    Log.i(
        "Default Date",
        DateFormat.format("EEE, MMM d, yyyy", defaultSelectedDate)
            .toString()
    )

    horizontalCalendar!!.calendarListener = object : HorizontalCalendarListener() {
      override fun onDateSelected(
        date: Calendar,
        position: Int
      ) {
        day = date
        onDayChange()
        val newDate = date.time
        val c = Calendar.getInstance()
        c.time = newDate
        c.add(Calendar.DATE, 1)

        Log.d("friday", c.time.toString())
//        val selectedDateStr =
//          DateFormat.format("EEE, MMM d, yyyy", date)
//              .toString()
//        Toast.makeText(context, "$selectedDateStr selected!", Toast.LENGTH_SHORT)
//            .show()
//        Log.i("onDateSelected", "$selectedDateStr - Position = $position")
      }
    }

    Coroutines.main {
      try {
        val elections = electionViewModel.elections.await()
        elections.observe(requireActivity(), Observer {
          if (it != null) {
            for (election in it) {
              addEvent(election)

            }
          }
        })
      } catch (e: IllegalStateException) {

      }
    }


    return rootView
  }

  private fun onScrollClick(v: View?) {
    showScrollTargetDialog()
  }

  private fun onDayChange() {
    dateTextView!!.text = dateFormat!!.format(day!!.time)
    onEventsChange()
  }

  private fun onEventsChange() {
    // The day view needs a list of event views and a corresponding list of event time ranges
    var eventViews: MutableList<View?>? = null
    var eventTimeRanges: MutableList<EventTimeRange?>? = null
    val events = allEvents!![day!!.timeInMillis]
    val gson = Gson()
    Log.d("on Event Change", gson.toJson(events) +" , day time in millis : " + day!!.timeInMillis )
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
        (eventView.findViewById<View>(R.id.event_location) as TextView).text = event.location
        eventView.setBackgroundColor(resources.getColor(event.color))

        // When an event is clicked, start a new draft event and show the edit event dialog
        eventView.setOnClickListener {
          editEventDraft = event
          editEventDate = day!!.clone() as Calendar
          editEventStartTime = Calendar.getInstance()
          editEventStartTime?.set(Calendar.HOUR_OF_DAY, editEventDraft!!.hour)
          editEventStartTime?.set(Calendar.MINUTE, editEventDraft!!.minute)
          editEventStartTime?.set(Calendar.SECOND, 0)
          editEventStartTime?.set(Calendar.MILLISECOND, 0)
          editEventEndTime = editEventStartTime?.clone() as Calendar
          editEventEndTime?.add(Calendar.MINUTE, editEventDraft!!.duration.toInt())
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
  }

  private fun showEditEventDialog(
    eventExists: Boolean,
    eventTitle: String?,
    eventLocation: String?,
    @ColorRes eventColor: Int
  ) {
    val view =
      layoutInflater.inflate(layout.edit_event_dialog, content, false)
    val titleTextView = view.findViewById<TextView>(R.id.edit_event_title)
    val locationTextView = view.findViewById<TextView>(R.id.edit_event_location)
    val dateButton =
      view.findViewById<Button>(R.id.edit_event_date)
    val startTimeButton =
      view.findViewById<Button>(R.id.edit_event_start_time)
    val endTimeButton =
      view.findViewById<Button>(R.id.edit_event_end_time)
    val redRadioButton = view.findViewById<RadioButton>(R.id.edit_event_red)
    val blueRadioButton = view.findViewById<RadioButton>(R.id.edit_event_blue)
    val orangeRadioButton = view.findViewById<RadioButton>(R.id.edit_event_orange)
    val greenRadioButton = view.findViewById<RadioButton>(R.id.edit_event_green)
    val purpleRadioButton = view.findViewById<RadioButton>(R.id.edit_event_purple)
    titleTextView.text = eventTitle
    locationTextView.text = eventLocation
    dateButton.text = dateFormat!!.format(editEventDate!!.time)
    dateButton.setOnClickListener {
      val listener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
        editEventDate!![Calendar.YEAR] = year
        editEventDate!![Calendar.MONTH] = month
        editEventDate!![Calendar.DAY_OF_MONTH] = dayOfMonth
        dateButton.text = dateFormat!!.format(editEventDate!!.time)
      }
      DatePickerDialog(
          context!!, listener, day!![Calendar.YEAR],
          day!![Calendar.MONTH], day!![Calendar.DAY_OF_MONTH]
      ).show()
    }
    startTimeButton.text = timeFormat!!.format(editEventStartTime!!.time)
    startTimeButton.setOnClickListener {
      val listener = OnTimeSetListener { view, hourOfDay, minute ->
        editEventStartTime!![Calendar.HOUR_OF_DAY] = hourOfDay
        editEventStartTime!![Calendar.MINUTE] = minute
        startTimeButton.text = timeFormat!!.format(editEventStartTime!!.time)
        if (!editEventEndTime!!.after(editEventStartTime)) {
          editEventEndTime = editEventStartTime!!.clone() as Calendar
          editEventEndTime?.add(Calendar.MINUTE, 30)
          endTimeButton.text = timeFormat!!.format(editEventEndTime?.getTime())
        }
      }
      TimePickerDialog(
          context, listener, editEventStartTime!![Calendar.HOUR_OF_DAY],
          editEventStartTime!![Calendar.MINUTE],
          DateFormat.is24HourFormat(context)
      ).show()
    }
    endTimeButton.text = timeFormat!!.format(editEventEndTime!!.time)
    endTimeButton.setOnClickListener {
      val listener = OnTimeSetListener { view, hourOfDay, minute ->
        editEventEndTime!![Calendar.HOUR_OF_DAY] = hourOfDay
        editEventEndTime!![Calendar.MINUTE] = minute
        if (!editEventEndTime!!.after(editEventStartTime)) {
          editEventEndTime = editEventStartTime!!.clone() as Calendar
          editEventEndTime?.add(Calendar.MINUTE, 30)
        }
        endTimeButton.text = timeFormat!!.format(editEventEndTime!!.time)
      }
      TimePickerDialog(
          context, listener, editEventEndTime!![Calendar.HOUR_OF_DAY],
          editEventEndTime!![Calendar.MINUTE],
          DateFormat.is24HourFormat(context)
      ).show()
    }
    if (eventColor == color.holo_blue_dark) {
      blueRadioButton.isChecked = true
    } else if (eventColor == color.holo_orange_dark) {
      orangeRadioButton.isChecked = true
    } else if (eventColor == color.holo_green_dark) {
      greenRadioButton.isChecked = true
    } else if (eventColor == color.holo_purple) {
      purpleRadioButton.isChecked = true
    } else {
      redRadioButton.isChecked = true
    }
    val builder =
      AlertDialog.Builder(context!!)

    // If the event already exists, we are editing it, otherwise we are adding a new event
    builder.setTitle(if (eventExists) string.edit_event else string.add_event)

    // When the event changes are confirmed, read the new values from the dialog and then add
    // this event to the list
    builder.setPositiveButton(android.R.string.ok) { dialog, which ->
      var events: MutableList<Event>? =
        allEvents!![editEventDate!!.timeInMillis] as MutableList<Event>
      if (events == null) {
        events = ArrayList()
        allEvents!!.put(editEventDate!!.timeInMillis, events)
      }
      val title = titleTextView.text
          .toString()
      val location = locationTextView.text
          .toString()
      val hour = editEventStartTime!![Calendar.HOUR_OF_DAY]
      val minute = editEventStartTime!![Calendar.MINUTE]
      val duration =
        (editEventEndTime!!.timeInMillis - editEventStartTime!!.timeInMillis) / 60000
      @ColorRes val color: Int = if (blueRadioButton.isChecked) {
      color.holo_blue_dark
    } else if (orangeRadioButton.isChecked) {
      color.holo_orange_dark
    } else if (greenRadioButton.isChecked) {
      color.holo_green_dark
    } else if (purpleRadioButton.isChecked) {
      color.holo_purple
    } else {
      color.holo_red_dark
    }
      events.add(Event(title, location, hour, minute, duration, color))
      onEditEventDismiss(true)
    }
    builder.setNegativeButton(
        android.R.string.cancel
    ) { dialog, which -> onEditEventDismiss(false) }

    // If the event already exists, provide a delete option
    if (eventExists) {
      builder.setNeutralButton(
          string.edit_event_delete
      ) { dialog, which -> onEditEventDismiss(true) }
    }
    builder.setOnCancelListener { onEditEventDismiss(false) }
    builder.setView(view)
    builder.show()
  }

  private fun showScrollTargetDialog() {
    val view =
      layoutInflater.inflate(layout.scroll_target_dialog, content, false)
    val timeButton =
      view.findViewById<Button>(R.id.scroll_target_time)
    val firstEventTopButton =
      view.findViewById<Button>(R.id.scroll_target_first_event_top)
    val firstEventBottomButton =
      view.findViewById<Button>(R.id.scroll_target_first_event_bottom)
    val lastEventTopButton =
      view.findViewById<Button>(R.id.scroll_target_last_event_top)
    val lastEventBottomButton =
      view.findViewById<Button>(R.id.scroll_target_last_event_bottom)
    val builder =
      AlertDialog.Builder(context!!)
    builder.setTitle(string.scroll_to)
    builder.setNegativeButton(android.R.string.cancel, null)
    builder.setView(view)
    val dialog = builder.show()
    timeButton.setOnClickListener {
      val listener = OnTimeSetListener { view, hourOfDay, minute ->
        val top = dayView!!.getHourTop(hourOfDay)
        val bottom = dayView!!.getHourBottom(hourOfDay)
        val y = top + (bottom - top) * minute / 60
        scrollView!!.smoothScrollTo(0, y)
        dialog.dismiss()
      }
      TimePickerDialog(
          context, listener, 0, 0,
          DateFormat.is24HourFormat(context)
      ).show()
    }
    firstEventTopButton.setOnClickListener {
      scrollView!!.smoothScrollTo(0, dayView!!.firstEventTop)
      dialog.dismiss()
    }
    firstEventBottomButton.setOnClickListener {
      scrollView!!.smoothScrollTo(0, dayView!!.firstEventBottom)
      dialog.dismiss()
    }
    lastEventTopButton.setOnClickListener {
      scrollView!!.smoothScrollTo(0, dayView!!.lastEventTop)
      dialog.dismiss()
    }
    lastEventBottomButton.setOnClickListener {
      scrollView!!.smoothScrollTo(0, dayView!!.lastEventBottom)
      dialog.dismiss()
    }
  }

  private fun addEvent(election: Election) {
    Log.d("friday", election.toString())
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
    val formattedStartingDate: Date? = formatter.parse(election.start!!)
    val formattedEndingDate: Date? = formatter.parse(election.end!!)
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val startCalendar = Calendar.getInstance()
    startCalendar.time = formattedStartingDate!!
    editEventDate = startCalendar.clone() as Calendar
    editEventStartTime = startCalendar.clone() as Calendar
    val endCalendar = Calendar.getInstance()
    endCalendar.time = formattedEndingDate!!
    editEventEndTime = endCalendar.clone() as Calendar
    var fStartDate = formattedStartingDate
    var count = 0
    var startDate = startCalendar.clone() as Calendar
    startDate.set(Calendar.HOUR, 0)
    startDate.set(Calendar.MINUTE, 0)
    startDate.set(Calendar.SECOND, 0)
    startDate.set(Calendar.HOUR_OF_DAY, 0)
    val endDate = endCalendar.clone() as Calendar
    endDate.set(Calendar.HOUR, 0)
    endDate.set(Calendar.MINUTE, 0)
    endDate.set(Calendar.SECOND, 0)
    endDate.set(Calendar.HOUR_OF_DAY, 0)
    while(fStartDate!!.before(formattedEndingDate)){
      count++
      val sDate = startCalendar.clone() as Calendar
      sDate.set(Calendar.HOUR, 0)
      sDate.set(Calendar.MINUTE, 0)
      sDate.set(Calendar.SECOND, 0)
      sDate.set(Calendar.HOUR_OF_DAY, 0)
      var events: MutableList<Event>? = allEvents!![sDate.timeInMillis] as MutableList?
      if (events == null) {
        events = ArrayList()
      }
      val title = election.name
      val location = election.description
      if(startDate == endDate){
        if(election._id == "5efdd5589e5954c79209b523"){
          Log.d("fStartDate", fStartDate.toString())
          Log.d("startCalendar", startCalendar.time.toString())
          Log.d("endCalendar", endCalendar.time.toString())
          Log.d("startDate", startDate.toString())
          Log.d("endDate", endDate.toString())
          Log.d("friday", "success")
        }
//        Log.d("fStartDate", fStartDate.toString())
//        Log.d("startCalendar", startCalendar.time.toString())
//        Log.d("editEventStartTime", editEventStartTime!!.time.toString())
//        Log.d("editEventEndTime", editEventEndTime!!.time.toString())
//        Log.d("endCalendar", endCalendar.time.toString())
//        Log.d("startDate", startDate!!.time.toString())
//        Log.d("endDate", endDate!!.time.toString())
//        Log.d("friday", "success")
        val hour = startCalendar[Calendar.HOUR_OF_DAY]
        val minute = startCalendar[Calendar.MINUTE]
        val duration =
          (endCalendar.timeInMillis - startCalendar.timeInMillis) / 60000
        @ColorRes val color: Int = color.holo_blue_dark
        events.add(Event(title, location, hour, minute, duration, color))
        val c = startCalendar.clone() as Calendar
        c.set(Calendar.HOUR, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.HOUR_OF_DAY, 0)
        allEvents!![c.timeInMillis] = events
        val gson = Gson()
        Log.d("all Events", gson.toJson(allEvents))
        onDayChange()
      }
      else if(startDate!!.before(endDate)){
        if(election._id == "5efdd5589e5954c79209b523"){
          Log.d("fStartDate", fStartDate.toString())
          Log.d("startCalendar", startCalendar.time.toString())
          Log.d("endCalendar", endCalendar.time.toString())
          Log.d("startDate", startDate.time.toString())
          Log.d("endDate", endDate.time.toString())
          Log.d("friday", "success")
        }
        val hour = startCalendar[Calendar.HOUR_OF_DAY]
        val minute = startCalendar[Calendar.MINUTE]
        val duration =
          (endCalendar.timeInMillis - startCalendar.timeInMillis) / 60000
        @ColorRes val color: Int = color.holo_blue_dark
        events.add(Event(title, location, hour, minute, duration, color))
        val c = startCalendar.clone() as Calendar
        c.set(Calendar.HOUR, 0)
        c.set(Calendar.MINUTE, 0)
        c.set(Calendar.SECOND, 0)
        c.set(Calendar.HOUR_OF_DAY, 0)
        allEvents!![c.timeInMillis] = events
        val gson = Gson()
        Log.d("all Events", gson.toJson(allEvents))
        onDayChange()
      }
      startCalendar.add(Calendar.DATE, 1)
      startCalendar.set(Calendar.HOUR, 0)
      startCalendar.set(Calendar.MINUTE, 0)
      startCalendar.set(Calendar.SECOND, 0)
      startCalendar.set(Calendar.HOUR_OF_DAY, 0)
      startDate = startCalendar
      fStartDate = startCalendar.time
    }
    Log.d("friday", count.toString())
  }

  private fun onEditEventDismiss(modified: Boolean) {
    if (modified && editEventDraft != null) {
      val events: MutableList<Event>? = allEvents!![day!!.timeInMillis] as MutableList<Event>?
      events!!.remove(editEventDraft!!)
    }
    editEventDraft = null
    onEventsChange()
  }

  /**
   * A data class used to represent an event on the calendar.
   */
  private class Event(
    val title: String?,
    val location: String?,
    val hour: Int,
    val minute: Int,
    val duration: Long,
    @field:ColorRes @param:ColorRes val color: Int
  )
}