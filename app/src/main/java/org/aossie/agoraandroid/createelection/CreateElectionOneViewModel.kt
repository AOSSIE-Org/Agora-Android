package org.aossie.agoraandroid.createelection

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.text.format.DateFormat
import android.view.View
import androidx.lifecycle.ViewModel
import java.util.*

class CreateElectionOneViewModel(private val context: Context) : ViewModel(){

     var mElectionName :String?  =null
     var mElectionDescription :String? = null
      var mStartDate :String? = null
    var mEndDate : String? =null
    private var sDay =0
    private var sMonth =0
    private var sYear =0
    private var eDay =0
    private var eMonth = 0
    private var eYear = 0


    var electionListener: ElectionListener?=null

    fun validator ():Boolean{

        var ans = true

        if(mElectionName.isNullOrEmpty())
        {
            electionListener?.nameError("Please Enter User Name")
            ans=false
        }

        if(mElectionDescription.isNullOrEmpty())
        {
            electionListener?.descError("Please Enter Description")
            ans=false
        }

        if(mStartDate.isNullOrEmpty())
        {
            electionListener?.startDateError("Please Select Start Date")
            ans=false
        }

        if(mEndDate.isNullOrEmpty())
        {
            electionListener?.startDateError("Please Select End Date")
            ans=false
        }

        return ans


    }


    fun handleStartDateTime(view :View)
    {

        val calendar = Calendar.getInstance()
        val YEAR = calendar[Calendar.YEAR]
        val MONTH = calendar[Calendar.MONTH]
        val DATE = calendar[Calendar.DATE]
        val HOUR = calendar[Calendar.HOUR]
        val MINUTE = calendar[Calendar.MINUTE]
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mStartDate = "$year-$month-$dayOfMonth"
           sDay = dayOfMonth
            sMonth = month
            sYear = year
        }, YEAR, MONTH, DATE)
        val timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            mStartDate = "$mStartDate,$hourOfDay:$minute"

            //Formatting the starting date in Date-Time format
            val calendar2 = Calendar.getInstance()
            calendar2[Calendar.HOUR_OF_DAY] = hourOfDay
            calendar2[Calendar.MINUTE] = minute
            calendar2[Calendar.YEAR] = sYear
            calendar2[Calendar.MONTH] = sMonth
            calendar2[Calendar.DAY_OF_MONTH] = sDay
            val charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar2)
        }, HOUR, MINUTE, true)
        timePickerDialog.show()
        datePickerDialog.show()

    }

    fun handleEndDateTime(view:View)
    {

        val calendar = Calendar.getInstance()
        val YEAR = calendar[Calendar.YEAR]
        val MONTH = calendar[Calendar.MONTH]
        val DATE = calendar[Calendar.DATE]
        val HOUR = calendar[Calendar.HOUR]
        val MINUTE = calendar[Calendar.MINUTE]
        val datePickerDialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            mEndDate = "$year-$month-$dayOfMonth"
            eYear = year
            eMonth = month
            eDay = dayOfMonth
        }, YEAR, MONTH, DATE)
        val timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
            mEndDate = "$mEndDate,$hourOfDay:$minute"

            //Formatting the ending date in Date-Time format
            val calendar3 = Calendar.getInstance()
            calendar3[Calendar.HOUR_OF_DAY] = hourOfDay
            calendar3[Calendar.MINUTE] = minute
            calendar3[Calendar.YEAR] = eYear
            calendar3[Calendar.MONTH] = eMonth
            calendar3[Calendar.DAY_OF_MONTH] = eDay
            val charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss'Z'", calendar3)
        }, HOUR, MINUTE, true)
        timePickerDialog.show()
        datePickerDialog.show()


    }

    fun onNextButtonClick(view: View)
    {
        if(!validator())
        {
            electionListener?.onFailure("Fields Not filled Correctly")
            return
        }

    }








}