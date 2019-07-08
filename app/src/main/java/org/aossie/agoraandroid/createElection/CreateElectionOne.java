package org.aossie.agoraandroid.createElection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;

import java.util.Calendar;

@SuppressWarnings("ConstantConditions")
public class CreateElectionOne extends AppCompatActivity {
    private TextInputLayout mNameTextLayout, mDescriptionTextLayout, mStartDateTextLayout, mEndDateTextLayout;
    private String electionName;
    private String electionDescription;
    private String startDate;
    private String endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_one);
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
                electionName = mNameTextLayout.getEditText().getText().toString();
                electionDescription = mDescriptionTextLayout.getEditText().getText().toString();
                startDate = mStartDateTextLayout.getEditText().getText().toString();
                endDate = mEndDateTextLayout.getEditText().getText().toString();
                if (electionName.isEmpty())
                    mNameTextLayout.setError("Please enter Election Name");
                else mNameTextLayout.setError(null);

                if (electionDescription.isEmpty()) {
                    mDescriptionTextLayout.setError("Please enter description");
                } else mDescriptionTextLayout.setError(null);

                if (startDate.isEmpty()) {
                    mStartDateTextLayout.setError("Please enter start date");
                } else mStartDateTextLayout.setError(null);

                if (endDate.isEmpty()) {
                    mEndDateTextLayout.setError("PLease enter end date");
                } else {
                    mEndDateTextLayout.setError(null);
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
                startDate = year + "/" + month + "/" + dayOfMonth;
                mStartDateTextLayout.getEditText().setText(startDate);
            }
        }, YEAR, MONTH, DATE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                startDate = "," + hourOfDay + ":" + minute;
                mStartDateTextLayout.getEditText().append(startDate);
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
                endDate = year + "/" + month + "/" + dayOfMonth;
                mEndDateTextLayout.getEditText().setText(endDate);
            }
        }, YEAR, MONTH, DATE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                endDate = "," + hourOfDay + ":" + minute;
                mEndDateTextLayout.getEditText().append(endDate);
            }
        }, HOUR, MINUTE, true);
        timePickerDialog.show();
        datePickerDialog.show();


    }
}
