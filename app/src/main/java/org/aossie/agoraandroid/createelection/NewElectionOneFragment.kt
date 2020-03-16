package org.aossie.agoraandroid.createelection

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_new_election_one.*
import org.aossie.agoraandroid.R
import java.util.Calendar

class NewElectionOneFragment(private val callingActivity: NewElectionActivity) : Fragment() {
  private var sDay = 0
  private var sMonth:Int = 0
  private var sYear:Int = 0
  private var eDay = 0
  private var eMonth:Int = 0
  private var eYear:Int = 0

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_new_election_one, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    start_date_ibtn.setOnClickListener {
      handleStartDateTime()
    }
    end_date_ibtn.setOnClickListener {
      handleEndDateTime()
    }
    submit_details_btn.setOnClickListener {
      callingActivity.mElectionName = election_name_til.editText!!.text.toString()
      callingActivity.mElectionDescription = election_description_til.editText!!.text.toString()
      callingActivity.mStartDate = start_date_til.editText!!.text.toString()
      callingActivity.mEndDate = end_date_til.editText!!.text.toString()

      if (callingActivity.mElectionName!!.isEmpty()) {
        election_name_til.editText!!.error = "Please enter Election Name"
      } else {
        election_name_til.editText!!.error = null
      }

      if (callingActivity.mElectionDescription!!.isEmpty()) {
        election_description_til.editText!!.error = "Please enter description"
      } else {
        election_description_til.editText!!.error = null
      }

      if (callingActivity.mStartDate!!.isEmpty()) {
        start_date_til.editText!!.error = "Please enter start date"
      } else {
        start_date_til.editText!!.error = null
      }

      if (callingActivity.mEndDate!!.isEmpty()) {
        end_date_til.editText!!.error = "Please enter end date"
      } else {
        end_date_til.editText!!.error = null
      }

      if(!callingActivity.mElectionName.isNullOrEmpty() &&
          !callingActivity.mElectionDescription.isNullOrEmpty() &&
          !callingActivity.mStartDate.isNullOrEmpty() &&
          !callingActivity.mEndDate.isNullOrEmpty()) {
        election_name_til.editText!!.error = null
        election_description_til.editText!!.error = null
        start_date_til.editText!!.error = null
        end_date_til.editText!!.error = null
        callingActivity.nextStep()
      }
    }
  }

  private fun handleStartDateTime() {
    val calendar = Calendar.getInstance()
    val mYear = calendar[Calendar.YEAR]
    val mMonth = calendar[Calendar.MONTH]
    val mDate = calendar[Calendar.DATE]
    val mHour = calendar[Calendar.HOUR]
    val mMinute = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
        callingActivity.mStartDate = "$year-$month-$dayOfMonth"
        sDay = dayOfMonth
        sMonth = month
        sYear = year
      }, mYear, mMonth, mDate)
    val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { _, hourOfDay, minute ->
      callingActivity.mStartDate = "${callingActivity.mStartDate},$hourOfDay:$minute"
      start_date_til.editText!!.setText(callingActivity.mStartDate)
      //Formatting the starting date in Date-Time format
      val calendar2 = Calendar.getInstance()
      calendar2[Calendar.HOUR_OF_DAY] = hourOfDay
      calendar2[Calendar.MINUTE] = minute
      calendar2[Calendar.YEAR] = sYear
      calendar2[Calendar.MONTH] = sMonth
      calendar2[Calendar.DAY_OF_MONTH] = sDay
      val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
    }, mHour, mMinute, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

  private fun handleEndDateTime() {
    val calendar = Calendar.getInstance()
    val mYear = calendar[Calendar.YEAR]
    val mMonth = calendar[Calendar.MONTH]
    val mDate = calendar[Calendar.DATE]
    val mHour = calendar[Calendar.HOUR]
    val mMinute = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
        callingActivity.mEndDate = "$year-$month-$dayOfMonth"
        eYear = year
        eMonth = month
        eDay = dayOfMonth
      }, mYear, mMonth, mDate)
    val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { _, hourOfDay, minute ->
      callingActivity.mEndDate = "${callingActivity.mEndDate},$hourOfDay:$minute"
      end_date_til.editText!!.setText(callingActivity.mEndDate)
      //Formatting the ending date in Date-Time format
      val calendar3 = Calendar.getInstance()
      calendar3[Calendar.HOUR_OF_DAY] = hourOfDay
      calendar3[Calendar.MINUTE] = minute
      calendar3[Calendar.YEAR] = eYear
      calendar3[Calendar.MONTH] = eMonth
      calendar3[Calendar.DAY_OF_MONTH] = eDay
      val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
    }, mHour, mMinute, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

}
