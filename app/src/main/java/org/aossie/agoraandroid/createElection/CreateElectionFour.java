package org.aossie.agoraandroid.createElection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.utilities.SharedPrefs2;

public class CreateElectionFour extends AppCompatActivity {
    SharedPrefs2 sharedPrefs2;
    CreateElectionViewModel createElectionViewModel;
    CheckBox isInvite, isRealTime;
    Boolean mFinalIsInvite, mFinalIsRealTime, voterListVisibility;
    RadioButton radioButtonListVoters, radioButtonBallots;
    RadioGroup radioGroupListVoters, radioGroupBallots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefs2=new SharedPrefs2(getApplication());
        createElectionViewModel = new CreateElectionViewModel(getApplication(), this);
        setContentView(R.layout.activity_create_election_four);
        Button mSubmitButton = findViewById(R.id.button_submit_details);
        isInvite = findViewById(R.id.check_invite_voters);
        isRealTime = findViewById(R.id.check_real_time_results);
        radioGroupListVoters = findViewById(R.id.radioGroup2);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroupListVoters.getCheckedRadioButtonId();
                radioButtonListVoters = findViewById(radioId);
                if (radioButtonListVoters.getText().toString().equals("Only me")) {
                    voterListVisibility = false;
                } else voterListVisibility = true;
                mFinalIsInvite = isInvite.isChecked();
                mFinalIsRealTime = isRealTime.isChecked();
                sharedPrefs2.saveIsInvite(mFinalIsInvite);
                sharedPrefs2.saveIsRealTime(mFinalIsRealTime);
                sharedPrefs2.saveVoterListVisibility(voterListVisibility);
                createElectionViewModel.createElection();
            }
        });
    }
}
