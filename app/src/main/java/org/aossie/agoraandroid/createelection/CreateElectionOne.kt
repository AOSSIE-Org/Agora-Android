package org.aossie.agoraandroid.createelection

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_create_election_one.*
import okhttp3.internal.Internal.instance
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.databinding.ActivityCreateElectionOneBinding
import org.aossie.agoraandroid.utilities.toast
import java.util.*

class CreateElectionOne : AppCompatActivity(),ElectionListener{
    private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
    private var mNameTextLayout: TextInputLayout? = null
    private var mDescriptionTextLayout: TextInputLayout? = null
    private var mStartDateTextLayout: TextInputLayout? = null
    private var mEndDateTextLayout: TextInputLayout? = null
    private var sDay = 0
    private var sMonth = 0
    private var sYear = 0
    private var eDay = 0
    private var eMonth = 0
    private var eYear = 0
    private var mElectionName: String? = null
    private var mElectionDescription: String? = null
    private var mStartDate: String? = null
    private var mEndDate: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_election_one)

        val factory = CreateElectionOneViewModelFactory(this)
        val binding :ActivityCreateElectionOneBinding = DataBindingUtil.setContentView(this,R.layout.activity_create_election_one)
        val viewModel =ViewModelProviders.of(this,factory).get(CreateElectionOneViewModel::class.java)
        binding.electiononeviewmodel = viewModel

        viewModel.electionListener =this
        electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(application)

       // mNameTextLayout = findViewById(R.id.election_name_til)
        //mDescriptionTextLayout = findViewById(R.id.election_description_til)
        // mStartDateTextLayout = findViewById(R.id.start_date_til)
        // mEndDateTextLayout = findViewById(R.id.end_date_til)

      //  val mStartDateImageButton = findViewById<ImageButton>(R.id.start_date_ibtn)
        // val mEndDateImageButton = findViewById<ImageButton>(R.id.end_date_ibtn)
        // val mNextButton = findViewById<Button>(R.id.submit_details_btn)
     //   mStartDateImageButton.setOnClickListener { handleStartDateTime() }
      //  mEndDateImageButton.setOnClickListener { handleEndDateTime() }

       /* mNextButton.setOnClickListener {
            mElectionName  = mNameTextLayout.getEditText()!!.text.toString()
            mElectionDescription = mDescriptionTextLayout.getEditText()!!.text.toString()
            mStartDate = mStartDateTextLayout.getEditText()!!.text.toString()
            mEndDate = mEndDateTextLayout.getEditText()!!.text.toString()
            if (mElectionName!!.isEmpty()) {
                mNameTextLayout.setError("Please enter Election Name")
            } else {
                mNameTextLayout.setError(null)
            }
            if (mElectionDescription!!.isEmpty()) {
                mDescriptionTextLayout.setError("Please enter description")
            } else {
                mDescriptionTextLayout.setError(null)
            }
            if (mStartDate!!.isEmpty()) {
                mStartDateTextLayout.setError("Please enter start date")
            } else {
                mStartDateTextLayout.setError(null)
            }
            if (mEndDate!!.isEmpty()) {
                mEndDateTextLayout.setError("PLease enter end date")
            } else {
                mEndDateTextLayout.setError(null)
                electionDetailsSharedPrefs!!.saveElectionName(mElectionName)
                electionDetailsSharedPrefs!!.saveElectionDesc(mElectionDescription)
                startActivity(Intent(this@CreateElectionOne, CreateElectionTwo::class.java))
            }
        }*/
    }

    private fun handleStartDateTime() {
        val calendar = Calendar.getInstance()
        val YEAR = calendar[Calendar.YEAR]
        val MONTH = calendar[Calendar.MONTH]
        val DATE = calendar[Calendar.DATE]
        val HOUR = calendar[Calendar.HOUR]
        val MINUTE = calendar[Calendar.MINUTE]
        val datePickerDialog = DatePickerDialog(this, OnDateSetListener { view, year, month, dayOfMonth ->
            mStartDate = "$year-$month-$dayOfMonth"
            sDay = dayOfMonth
            sMonth = month
            sYear = year
        }, YEAR, MONTH, DATE)
        val timePickerDialog = TimePickerDialog(this, OnTimeSetListener { view, hourOfDay, minute ->
            mStartDate = "$mStartDate,$hourOfDay:$minute"
            mStartDateTextLayout!!.editText!!.setText(mStartDate)
            //Formatting the starting date in Date-Time format
            val calendar2 = Calendar.getInstance()
            calendar2[Calendar.HOUR_OF_DAY] = hourOfDay
            calendar2[Calendar.MINUTE] = minute
            calendar2[Calendar.YEAR] = sYear
            calendar2[Calendar.MONTH] = sMonth
            calendar2[Calendar.DAY_OF_MONTH] = sDay
            val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
            electionDetailsSharedPrefs!!.saveStartTime(charSequence.toString())
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
        val datePickerDialog = DatePickerDialog(this, OnDateSetListener { view, year, month, dayOfMonth ->
            mEndDate = "$year-$month-$dayOfMonth"
            eYear = year
            eMonth = month
            eDay = dayOfMonth
        }, YEAR, MONTH, DATE)
        val timePickerDialog = TimePickerDialog(this, OnTimeSetListener { view, hourOfDay, minute ->
            mEndDate = "$mEndDate,$hourOfDay:$minute"
            mEndDateTextLayout!!.editText!!.setText(mEndDate)
            //Formatting the ending date in Date-Time format
            val calendar3 = Calendar.getInstance()
            calendar3[Calendar.HOUR_OF_DAY] = hourOfDay
            calendar3[Calendar.MINUTE] = minute
            calendar3[Calendar.YEAR] = eYear
            calendar3[Calendar.MONTH] = eMonth
            calendar3[Calendar.DAY_OF_MONTH] = eDay
            val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
            electionDetailsSharedPrefs!!.saveEndTime(charSequence2.toString())
        }, HOUR, MINUTE, true)
        timePickerDialog.show()
        datePickerDialog.show()

    }



    override fun onSuccess() {
        startActivity(Intent(this,CreateElectionTwo::class.java))
    }

    override fun onFailure(message: String) {

            toast(message)
    }

    override fun nameError(message: String) {

        election_name_til.error = message

    }

    override fun descError(message: String) {

        election_description_til.error = message

    }

    override fun startDateError(message: String) {

        start_date_til.error = message

    }

    override fun endDateError(message: String) {

        end_date_til.error   =message

    }
}