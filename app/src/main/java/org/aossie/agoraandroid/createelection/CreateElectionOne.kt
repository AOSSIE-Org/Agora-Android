package org.aossie.agoraandroid.createelection

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.material.textfield.TextInputLayout
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.CreateElectionOneFragmentBinding
import java.util.*

class CreateElectionOne : Fragment() {

  companion object {
    fun newInstance() = CreateElectionOne()
  }

  private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
  private var mNameTextLayout: TextInputLayout? = null
  private var mDescriptionTextLayout: TextInputLayout? = null
  private var mStartDateTextLayout: TextInputLayout? = null
  private var mEndDateTextLayout: TextInputLayout? = null
  private var sDay: Int = 0
  private var sMonth: Int = 0
  private var sYear: Int = 0
  private var eDay: Int = 0
  private var eMonth: Int = 0
  private var eYear: Int = 0
  private var mElectionName: String? = null
  private var mElectionDescription: String? = null
  private var mStartDate: String? = null
  private var mEndDate: String? = null

  private lateinit var viewModel: CreateElectionOneViewModel
  private lateinit var binding: CreateElectionOneFragmentBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    viewModel = ViewModelProviders.of(this)
        .get(CreateElectionOneViewModel::class.java)
    binding =
      DataBindingUtil.inflate(inflater, R.layout.create_election_one_fragment, container, false)
    binding.vm2 = viewModel
    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(context!!)

    mNameTextLayout = binding.electionNameTil
    mDescriptionTextLayout = binding.electionDescriptionTil
    mStartDateTextLayout = binding.startDateTil
    mEndDateTextLayout = binding.endDateTil
    val mStartDateImageButton = binding.startDateIbtn
    val mEndDateImageButton = binding.endDateIbtn
    val mNextButton = binding.submitDetailsBtn

    mStartDateImageButton!!.setOnClickListener(View.OnClickListener { handleStartDateTime() })
    mEndDateImageButton!!.setOnClickListener(View.OnClickListener { handleEndDateTime() })
    mNextButton!!.setOnClickListener(View.OnClickListener {
      mElectionName = mNameTextLayout?.editText!!.text.toString()
      mElectionDescription = mDescriptionTextLayout?.editText!!.text.toString()
      mStartDate = mStartDateTextLayout?.editText!!.text.toString()
      mEndDate = mEndDateTextLayout?.editText!!.text.toString()
      if (mElectionName!!.isEmpty()) {
        mNameTextLayout!!.error = "Please enter Election Name"
      } else {
        mNameTextLayout!!.error = null
      }

      if (mElectionDescription!!.isEmpty()) {
        mDescriptionTextLayout!!.error = "Please enter description"
      } else {
        mDescriptionTextLayout!!.error = null
      }

      if (mStartDate!!.isEmpty()) {
        mStartDateTextLayout!!.error = "Please enter start date"
      } else {
        mStartDateTextLayout!!.error = null
      }

      if (mEndDate!!.isEmpty()) {
        mEndDateTextLayout!!.error = "PLease enter end date"
      } else {
        mEndDateTextLayout?.error = null

      }



      if (!mElectionName!!.isEmpty() && !mElectionDescription!!.isEmpty() && !mStartDate!!.isEmpty() && !mEndDate!!.isEmpty()) {
        electionDetailsSharedPrefs!!.saveElectionName(mElectionName!!)
        electionDetailsSharedPrefs!!.saveElectionDesc(mElectionDescription!!)
        Navigation.findNavController(it)
            .navigate(R.id.action_election1_to_election2)
      }

    })
    return binding.root
  }

  private fun handleStartDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar.get(Calendar.YEAR)
    val MONTH = calendar.get(Calendar.MONTH)
    val DATE = calendar.get(Calendar.DATE)
    val HOUR = calendar.get(Calendar.HOUR)
    val MINUTE = calendar.get(Calendar.MINUTE)
    val datePickerDialog = DatePickerDialog(
        context!!,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
          mStartDate = "$year-$month-$dayOfMonth"
          sDay = dayOfMonth
          sMonth = month
          sYear = year
        }, YEAR, MONTH, DATE
    )

    val timePickerDialog = TimePickerDialog(
        context!!,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
          mStartDate = mStartDate + "," + hourOfDay + ":" + minute
          mStartDateTextLayout!!.editText!!.setText(mStartDate)

          //Formatting the starting date in Date-Time format
          val calendar2 = Calendar.getInstance()
          calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay)
          calendar2.set(Calendar.MINUTE, minute)
          calendar2.set(Calendar.YEAR, sYear)
          calendar2.set(Calendar.MONTH, sMonth)
          calendar2.set(Calendar.DAY_OF_MONTH, sDay)
          val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)

          electionDetailsSharedPrefs!!.saveStartTime(charSequence.toString())
        }, HOUR, MINUTE, true
    )
    timePickerDialog.show()
    datePickerDialog.show()
  }

  private fun handleEndDateTime() {
    val calendar = Calendar.getInstance()
    val YEAR = calendar.get(Calendar.YEAR)
    val MONTH = calendar.get(Calendar.MONTH)
    val DATE = calendar.get(Calendar.DATE)
    val HOUR = calendar.get(Calendar.HOUR)
    val MINUTE = calendar.get(Calendar.MINUTE)
    val datePickerDialog = DatePickerDialog(
        context!!,
        DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
          mEndDate = "$year-$month-$dayOfMonth"
          eYear = year
          eMonth = month
          eDay = dayOfMonth
        }, YEAR, MONTH, DATE
    )

    val timePickerDialog = TimePickerDialog(
        context!!,
        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
          mEndDate = mEndDate + "," + hourOfDay + ":" + minute
          mEndDateTextLayout!!.editText!!.setText(mEndDate)

          //Formatting the ending date in Date-Time format
          val calendar3 = Calendar.getInstance()
          calendar3.set(Calendar.HOUR_OF_DAY, hourOfDay)
          calendar3.set(Calendar.MINUTE, minute)
          calendar3.set(Calendar.YEAR, eYear)
          calendar3.set(Calendar.MONTH, eMonth)
          calendar3.set(Calendar.DAY_OF_MONTH, eDay)
          val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)

          electionDetailsSharedPrefs!!.saveEndTime(charSequence2.toString())
        }, HOUR, MINUTE, true
    )
    timePickerDialog.show()
    datePickerDialog.show()
  }

}







