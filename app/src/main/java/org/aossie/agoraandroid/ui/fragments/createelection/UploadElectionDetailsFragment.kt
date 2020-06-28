package org.aossie.agoraandroid.ui.fragments.createelection

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.et_election_description
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.et_election_name
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.et_end_date
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.et_start_date
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.start_date_ibtn
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.start_date_til
import kotlinx.android.synthetic.main.fragment_upload_election_details.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInActivity
import org.aossie.agoraandroid.utilities.errorDialog
import java.util.Calendar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UploadElectionDetailsFragment
  @Inject
  constructor(
    private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs
  ): Fragment() {

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
  private var calendar2: Calendar? = null
  private var calendar3: Calendar ? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_upload_election_details, container, false)

    rootView.start_date_ibtn.setOnClickListener { handleStartDateTime() }

    rootView.end_date_ibtn.setOnClickListener { handleEndDateTime() }

    rootView.et_election_name.addTextChangedListener(textWatcher)

    rootView.et_election_description.addTextChangedListener(textWatcher)

    rootView.et_start_date.addTextChangedListener(textWatcher)

    rootView.et_end_date.addTextChangedListener(textWatcher)

    rootView.submit_details_btn.setOnClickListener {
      mElectionName = rootView.election_name_til.editText?.text.toString()
      mElectionDescription = rootView.election_description_til.editText?.text.toString()
      mStartDate = rootView.start_date_til.editText?.text.toString()
      mEndDate = rootView.end_date_til.editText?.text.toString()
      if(validateInputs()){
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

  private fun validateInputs(): Boolean{
    var isValid = false
    if (mElectionName!!.isEmpty()) {
      rootView.election_name_til.error = "Please enter Election Name"
      isValid = false
    } else {
      rootView.election_name_til.error = null
      isValid = true
    }
    if (mElectionDescription!!.isEmpty()) {
      rootView.election_description_til.error = "Please enter description"
      isValid = false
    } else {
      rootView.election_description_til.error = null
      isValid = true
    }
    if (mStartDate!!.isEmpty()) {
      rootView.start_date_til.error = "Please enter start date"
      isValid = false
    } else {
      rootView.start_date_til.error = null
      isValid = true
    }
    if (mEndDate!!.isEmpty()) {
      rootView.end_date_til.error = "Please enter end date"
      isValid = false
    }else{
      rootView.end_date_til.error = null
      isValid = true
    }
    isValid = if(calendar3!!.before(calendar2)){
      rootView.errorDialog("End date should be after starting date and time i.e. $mStartDate")
      false
    }else{
      true
    }
    return isValid
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
        mStartDate = "$year/$month/$dayOfMonth"
        sDay = dayOfMonth
        sMonth = month
        sYear = year
      }, YEAR, MONTH, DATE)
    val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { view, hourOfDay, minute ->
      mStartDate = if(hourOfDay < 10 && minute < 10)  "$mStartDate at 0$hourOfDay:0$minute"
      else if(hourOfDay < 10) "$mStartDate at 0$hourOfDay:$minute"
      else if (minute < 10) "$mStartDate at $hourOfDay:0$minute"
      else "$mStartDate at $hourOfDay:$minute"
      //Formatting the starting date in Date-Time format
      calendar2 = Calendar.getInstance()
      calendar2?.set(Calendar.HOUR_OF_DAY, hourOfDay)
      calendar2?.set(Calendar.MINUTE, minute)
      calendar2?.set(Calendar.YEAR, sYear)
      calendar2?.set(Calendar.MONTH, sMonth)
      calendar2?.set(Calendar.DAY_OF_MONTH, sDay)
      if(calendar2 != null && calendar2!!.before(Calendar.getInstance())) {
        rootView.errorDialog("Start date should be after current date and time")
      }else {
        rootView.start_date_til.editText?.setText(mStartDate)
        val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
        electionDetailsSharedPrefs.saveStartTime(charSequence.toString())
      }
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
        mEndDate = "$year/$month/$dayOfMonth"
        eYear = year
        eMonth = month
        eDay = dayOfMonth
      }, YEAR, MONTH, DATE)
    val timePickerDialog = TimePickerDialog(context!!, OnTimeSetListener { view, hourOfDay, minute ->
      //Formatting the ending date in Date-Time format
      mEndDate = if(hourOfDay < 10 && minute < 10)  "$mEndDate at 0$hourOfDay:0$minute"
      else if(hourOfDay < 10) "$mEndDate at 0$hourOfDay:$minute"
      else if (minute < 10) "$mEndDate at $hourOfDay:0$minute"
      else "$mEndDate at $hourOfDay:$minute"
      calendar3 = Calendar.getInstance()
      calendar3?.set(Calendar.HOUR_OF_DAY, hourOfDay)
      calendar3?.set(Calendar.MINUTE, minute)
      calendar3?.set(Calendar.YEAR, eYear)
      calendar3?.set(Calendar.MONTH, eMonth)
      calendar3?.set(Calendar.DAY_OF_MONTH, eDay)
      if(calendar3 != null && calendar3!!.before(Calendar.getInstance())){
        rootView.errorDialog("End date should be after current date and time")
      }else {
        rootView.end_date_til.editText?.setText(mEndDate)
        val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
        electionDetailsSharedPrefs.saveEndTime(charSequence2.toString())
      }
    }, HOUR, MINUTE, true)
    timePickerDialog.show()
    datePickerDialog.show()
  }

//  private fun handleStartDateTime() {
//    val calendar = Calendar.getInstance()
//    val YEAR = calendar[Calendar.YEAR]
//    val MONTH = calendar[Calendar.MONTH]
//    val DATE = calendar[Calendar.DATE]
//    val HOUR = calendar[Calendar.HOUR]
//    val MINUTE = calendar[Calendar.MINUTE]
//    val datePickerDialog =
//      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
//        mStartDate = "$year/$month/$dayOfMonth"
//        sDay = dayOfMonth
//        sMonth = month
//        sYear = year
//      }, YEAR, MONTH, DATE)
//    val timePickerDialog = TimePickerDialog(context, OnTimeSetListener { view, hourOfDay, minute ->
//      mStartDate = if(hourOfDay < 10 && minute < 10)  "$mStartDate at 0$hourOfDay:0$minute"
//      else if(hourOfDay < 10) "$mStartDate at 0$hourOfDay:$minute"
//      else if (minute < 10) "$mStartDate at $hourOfDay:0$minute"
//      else "$mStartDate at $hourOfDay:$minute"
//      //Formatting the starting date in Date-Time format
//      calendar2 = Calendar.getInstance()
//      calendar2?.set(Calendar.HOUR_OF_DAY, hourOfDay)
//      calendar2?.set(Calendar.MINUTE, minute)
//      calendar2?.set(Calendar.YEAR, sYear)
//      calendar2?.set(Calendar.MONTH, sMonth)
//      calendar2?.set(Calendar.DAY_OF_MONTH, sDay)
//      if(calendar2 != null && calendar3 != null && calendar2!!.after(calendar3)){
//        rootView.errorDialog("Start date should be before ending date and time i.e. $mEndDate")
//        calendar2 = null
//      } else if(calendar2 != null && calendar2!!.before(Calendar.getInstance())) {
//        rootView.errorDialog("Start date should be after current date and time")
//        calendar2 = null
//      }else {
//        rootView.start_date_til.editText?.setText(mStartDate)
//        val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
//        electionDetailsSharedPrefs.saveStartTime(charSequence.toString())
//      }
//    }, HOUR, MINUTE, true)
//    timePickerDialog.show()
//    datePickerDialog.show()
//  }
//
//  private fun handleEndDateTime() {
//    val calendar = Calendar.getInstance()
//    val YEAR = calendar[Calendar.YEAR]
//    val MONTH = calendar[Calendar.MONTH]
//    val DATE = calendar[Calendar.DATE]
//    val HOUR = calendar[Calendar.HOUR]
//    val MINUTE = calendar[Calendar.MINUTE]
//    val datePickerDialog =
//      DatePickerDialog(context!!, OnDateSetListener { _, year, month, dayOfMonth ->
//        mEndDate = "$year/$month/$dayOfMonth"
//        eYear = year
//        eMonth = month
//        eDay = dayOfMonth
//      }, YEAR, MONTH, DATE)
//    val timePickerDialog = TimePickerDialog(context!!, OnTimeSetListener { view, hourOfDay, minute ->
//      //Formatting the ending date in Date-Time format
//      mEndDate = if(hourOfDay < 10 && minute < 10)  "$mEndDate at 0$hourOfDay:0$minute"
//      else if(hourOfDay < 10) "$mEndDate at 0$hourOfDay:$minute"
//      else if (minute < 10) "$mEndDate at $hourOfDay:0$minute"
//      else "$mEndDate at $hourOfDay:$minute"
//      calendar3 = Calendar.getInstance()
//      calendar3?.set(Calendar.HOUR_OF_DAY, hourOfDay)
//      calendar3?.set(Calendar.MINUTE, minute)
//      calendar3?.set(Calendar.YEAR, eYear)
//      calendar3?.set(Calendar.MONTH, eMonth)
//      calendar3?.set(Calendar.DAY_OF_MONTH, eDay)
//      if(calendar2 != null && calendar3 != null &&  calendar3!!.before(calendar2)){
//        rootView.errorDialog("End date should be after starting date and time i.e. $mStartDate")
//        calendar3 = null
//      } else if(calendar3 != null && calendar3!!.before(Calendar.getInstance())){
//        rootView.errorDialog("End date should be after current date and time")
//        calendar3 = null
//      }else {
//        rootView.end_date_til.editText?.setText(mEndDate)
//        val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
//        electionDetailsSharedPrefs.saveEndTime(charSequence2.toString())
//      }
//    }, HOUR, MINUTE, true)
//    timePickerDialog.show()
//    datePickerDialog.show()
//  }

  private val textWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val electionNameInput: String = rootView.et_election_name.text
          .toString()
          .trim()
      val electionDescriptionInput: String = rootView.et_election_description.text
          .toString()
          .trim()
      val startDateInput: String = rootView.et_start_date.text
          .toString()
          .trim()
      val endDateInput: String = rootView.et_end_date.text
          .toString()
          .trim()
      rootView.submit_details_btn.isEnabled = electionNameInput.isNotEmpty()
          && electionDescriptionInput.isNotEmpty()
          && startDateInput.isNotEmpty()
          && endDateInput.isNotEmpty()
    }
  }

}
