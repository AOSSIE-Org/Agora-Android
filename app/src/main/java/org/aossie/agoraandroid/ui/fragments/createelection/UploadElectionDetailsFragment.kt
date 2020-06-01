package org.aossie.agoraandroid.ui.fragments.createelection

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.election_description_til
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.election_name_til
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.end_date_ibtn
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.end_date_til
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.start_date_ibtn
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.start_date_til
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInActivity
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 */
class UploadElectionDetailsFragment : Fragment() {

  private lateinit var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs
  private var sDay = 0
  private  var sMonth:Int = 0
  private  var sYear:Int = 0
  private var eDay = 0
  private  var eMonth:Int = 0
  private  var eYear:Int = 0
  private var mElectionName: String? = null
  private var mElectionDescription: String? = null
  private var mStartDate: String? = null
  private var mEndDate: String? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_upload_election_details, container, false)

    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(activity!!.application)
    rootView.start_date_ibtn.setOnClickListener { handleStartDateTime() }
    rootView.end_date_ibtn.setOnClickListener { handleEndDateTime() }
    rootView.submit_details_btn.setOnClickListener {
      mElectionName = rootView.election_name_til.editText?.text.toString()
      mElectionDescription = rootView.election_description_til.editText?.text.toString()
      mStartDate = rootView.start_date_til.editText?.text.toString()
      mEndDate = rootView.end_date_til.editText?.text.toString()
      if (mElectionName!!.isEmpty()) {
        rootView.election_name_til.error = "Please enter Election Name"
      } else {
        rootView.election_name_til.error = null
      }
      if (mElectionDescription!!.isEmpty()) {
        rootView.election_description_til.error = "Please enter description"
      } else {
        rootView.election_description_til.error = null
      }
      if (mStartDate!!.isEmpty()) {
        rootView.start_date_til.error = "Please enter start date"
      } else {
        rootView.start_date_til.error = null
      }
      if (mEndDate!!.isEmpty()) {
        rootView.end_date_til.error = "Please enter end date"
      } else {
        hideKeyboardInActivity(activity as AppCompatActivity)
        rootView.end_date_til.error = null
        electionDetailsSharedPrefs.saveElectionName(mElectionName)
        electionDetailsSharedPrefs.saveElectionDesc(mElectionDescription)
        Navigation.findNavController(rootView)
            .navigate(UploadElectionDetailsFragmentDirections.actionUploadElectionDetailsFragmentToUploadCandidatesFragment())
      }
    }

    return rootView
  }

  private fun handleStartDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR]
    val MINUTE = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
        mStartDate = "$year-$month-$dayOfMonth"
        sDay = dayOfMonth
        sMonth = month
        sYear = year
      }, YEAR, MONTH, DATE)
    val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { view, hourOfDay, minute ->
      mStartDate = "$mStartDate,$hourOfDay:$minute"
      rootView.start_date_til.editText?.setText(mStartDate)

      //Formatting the starting date in Date-Time format
      val calendar2 = Calendar.getInstance()
      calendar2[Calendar.HOUR_OF_DAY] = hourOfDay
      calendar2[Calendar.MINUTE] = minute
      calendar2[Calendar.YEAR] = sYear
      calendar2[Calendar.MONTH] = sMonth
      calendar2[Calendar.DAY_OF_MONTH] = sDay
      val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
      electionDetailsSharedPrefs.saveStartTime(charSequence.toString())
    }, HOUR, MINUTE, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

  private fun handleEndDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar[Calendar.YEAR]
    val MONTH = calendar[Calendar.MONTH]
    val DATE = calendar[Calendar.DATE]
    val HOUR = calendar[Calendar.HOUR]
    val MINUTE = calendar[Calendar.MINUTE]
    val datePickerDialog =
      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
        mEndDate = "$year-$month-$dayOfMonth"
        eYear = year
        eMonth = month
        eDay = dayOfMonth
      }, YEAR, MONTH, DATE)
    val timePickerDialog = TimePickerDialog(context!!, OnTimeSetListener { view, hourOfDay, minute ->
      mEndDate = "$mEndDate,$hourOfDay:$minute"
      rootView.end_date_til.editText?.setText(mEndDate)

      //Formatting the ending date in Date-Time format
      val calendar3 = Calendar.getInstance()
      calendar3[Calendar.HOUR_OF_DAY] = hourOfDay
      calendar3[Calendar.MINUTE] = minute
      calendar3[Calendar.YEAR] = eYear
      calendar3[Calendar.MONTH] = eMonth
      calendar3[Calendar.DAY_OF_MONTH] = eDay
      val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
      electionDetailsSharedPrefs.saveEndTime(charSequence2.toString())
    }, HOUR, MINUTE, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

}
