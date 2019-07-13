package org.aossie.agoraandroid.createElection;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.aossie.agoraandroid.R;

public class CreateElectionFour extends AppCompatActivity {
    private ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
    private CreateElectionViewModel createElectionViewModel;
    private CheckBox isInvite;
    private CheckBox isRealTime;
    private Boolean mFinalIsInvite;
    private Boolean mFinalIsRealTime;
    private Boolean voterListVisibility;
    private RadioButton radioButtonListVoters;
    private RadioButton radioButtonBallots;
    private RadioGroup radioGroupListVoters;
    private RadioGroup radioGroupBallots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getApplication());
        createElectionViewModel = new CreateElectionViewModel(getApplication(), this);
        setContentView(R.layout.activity_create_election_four);
        Button mSubmitButton = findViewById(R.id.button_submit_details);
        isInvite = findViewById(R.id.check_invite_voters);
        isRealTime = findViewById(R.id.check_real_time_results);
        radioGroupListVoters = findViewById(R.id.radioGroup2);
        radioGroupBallots = findViewById(R.id.radioGroup);


        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                int radioId = radioGroupListVoters.getCheckedRadioButtonId();
                radioButtonListVoters = findViewById(radioId);

                int radioId1 = radioGroupBallots.getCheckedRadioButtonId();
                radioButtonBallots = findViewById(radioId1);


                voterListVisibility = !radioButtonListVoters.getText().toString().equals("Only me");

                mFinalIsInvite = isInvite.isChecked();
                mFinalIsRealTime = isRealTime.isChecked();
                electionDetailsSharedPrefs.saveIsInvite(mFinalIsInvite);
                electionDetailsSharedPrefs.saveIsRealTime(mFinalIsRealTime);
                electionDetailsSharedPrefs.saveVoterListVisibility(voterListVisibility);
                electionDetailsSharedPrefs.saveBallotVisibility(radioButtonBallots.getText().toString().trim());
                createElectionViewModel.createElection();
            }
        });
    }
}
