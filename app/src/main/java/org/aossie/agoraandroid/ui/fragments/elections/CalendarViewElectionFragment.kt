package org.aossie.agoraandroid.ui.fragments.elections

import android.graphics.Color
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.fragment.app.Fragment
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendar.Builder
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import org.aossie.agoraandroid.R
import java.util.Calendar

class CalendarViewElectionFragment : Fragment() {

  lateinit var rootView: View

  private var horizontalCalendar: HorizontalCalendar? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_calendar_view_election, container, false)

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
        val selectedDateStr =
          DateFormat.format("EEE, MMM d, yyyy", date)
              .toString()
        Toast.makeText(context, "$selectedDateStr selected!", Toast.LENGTH_SHORT)
            .show()
        Log.i("onDateSelected", "$selectedDateStr - Position = $position")
      }
    }

    return rootView
  }
}