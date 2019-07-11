package org.aossie.agoraandroid.createElection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.utilities.SharedPrefs2;

import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
public class CreateElectionOne extends AppCompatActivity {
    private SharedPrefs2 sharedPrefs2;
    private TextInputLayout mNameTextLayout, mDescriptionTextLayout, mStartDateTextLayout, mEndDateTextLayout;
    private int sDay, sMonth, sYear;
    private int eDay, eMonth, eYear;
    String mElectionName;
    String mElectionDescription;
    private String mStartDate;
    private String mEndDate;
    String mFinalStartDate, mFinalEndDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_one);
        sharedPrefs2 = new SharedPrefs2(getApplication());
        mNameTextLayout = findViewById(R.id.text_layout_election_name);
        mDescriptionTextLayout = findViewById(R.id.text_layout_election_description);
        mStartDateTextLayout = findViewById(R.id.text_layout_start_date);
        mEndDateTextLayout = findViewById(R.id.text_layout_end_date);
        ImageButton mStartDateImageButton = findViewById(R.id.imageButton_start_date);
        ImageButton mEndDateImageButton = findViewById(R.id.imageButton_end_date);
        Button mNextButton = findViewById(R.id.button_next_one);
        mStartDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartDateTime();

            }
        });
        mEndDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEndDateTime();

            }
        });
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mElectionName = mNameTextLayout.getEditText().getText().toString();
                mElectionDescription = mDescriptionTextLayout.getEditText().getText().toString();
                mStartDate = mStartDateTextLayout.getEditText().getText().toString();
                mEndDate = mEndDateTextLayout.getEditText().getText().toString();
                if (mElectionName.isEmpty())
                    mNameTextLayout.setError("Please enter Election Name");
                else mNameTextLayout.setError(null);

                if (mElectionDescription.isEmpty()) {
                    mDescriptionTextLayout.setError("Please enter description");
                } else mDescriptionTextLayout.setError(null);

                if (mStartDate.isEmpty()) {
                    mStartDateTextLayout.setError("Please enter start date");
                } else mStartDateTextLayout.setError(null);

                if (mEndDate.isEmpty()) {
                    mEndDateTextLayout.setError("PLease enter end date");
                } else {
                    mEndDateTextLayout.setError(null);
                    sharedPrefs2.saveElectionName(mElectionName);
                    sharedPrefs2.saveElectionDesc(mElectionDescription);
                    startActivity(new Intent(CreateElectionOne.this, CreateElectionTwo.class));

                }


            }
        });
    }

    private void handleStartDateTime() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mStartDate = year + "-" + month + "-" + dayOfMonth;
                sDay = dayOfMonth;
                sMonth = month;
                sYear = year;
            }
        }, YEAR, MONTH, DATE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mStartDate = mStartDate + "," + hourOfDay + ":" + minute;
                mStartDateTextLayout.getEditText().setText(mStartDate);
                //Formatting the starting date
                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);
                calendar2.set(Calendar.YEAR, sYear);
                calendar2.set(Calendar.MONTH, sMonth);
                calendar2.set(Calendar.DAY_OF_MONTH, sDay);
                CharSequence charSequence = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.687Z", calendar2);

                sharedPrefs2.saveStartTime(charSequence.toString());

            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
        datePickerDialog.show();


    }

    private void handleEndDateTime() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mEndDate = year + "-" + month + "-" + dayOfMonth;
                eYear = year;
                eMonth = month;
                eDay = dayOfMonth;
 //               mFinalEndDate = year + "-" + month + "-" + dayOfMonth;
            }
        }, YEAR, MONTH, DATE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mEndDate = mEndDate + "," + hourOfDay + ":" + minute;
                mEndDateTextLayout.getEditText().setText(mEndDate);
  //              mFinalEndDate = mFinalEndDate + "T" + hourOfDay + ":" + minute + ":00.871Z";
                //Formatting the ending date
                Calendar calendar3 = Calendar.getInstance();
                calendar3.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar3.set(Calendar.MINUTE, minute);
                calendar3.set(Calendar.YEAR, eYear);
                calendar3.set(Calendar.MONTH, eMonth);
                calendar3.set(Calendar.DAY_OF_MONTH, eDay);
                CharSequence charSequence2 = DateFormat.format("yyyy-MM-dd'T'HH:mm:ss.687Z", calendar3);

                sharedPrefs2.saveEndTime(charSequence2.toString());

                //        sharedPrefs2.saveEndTime(mFinalEndDate);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
        datePickerDialog.show();


    }
}
