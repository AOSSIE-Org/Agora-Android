package org.aossie.agoraandroid.displayelections;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.invitevoters.InviteVotersActivity;
import org.aossie.agoraandroid.result.ResultViewModel;
import org.aossie.agoraandroid.utilities.SharedPrefs;

public class ElectionActivity extends AppCompatActivity {
  private TextView mElectionName, mElectionDescription, mStartDate, mEndDate, mCandidateName,
      mStatus;
  private String id, status, token;
  private ConstraintLayout constraintLayout;
  private DisplayElectionViewModel displayElectionViewModel;
  private ResultViewModel resultViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_election);

    SharedPrefs sharedPrefs = new SharedPrefs(getApplicationContext());
    displayElectionViewModel = new DisplayElectionViewModel(getApplication(), this);
    resultViewModel = new ResultViewModel(getApplication(), this);
    token = sharedPrefs.getToken();
    mElectionName = findViewById(R.id.tv_election_name);
    mElectionDescription = findViewById(R.id.tv_description);
    mStartDate = findViewById(R.id.tv_start_date);
    mEndDate = findViewById(R.id.tv_end_date);
    mCandidateName = findViewById(R.id.tv_candidate_list);
    mStatus = findViewById(R.id.tv_status);
    constraintLayout = findViewById(R.id.constraint_layout);

    Button mDeleteElection = findViewById(R.id.button_delete);
    Button mInviteVoters = findViewById(R.id.button_invite_voters);
    Button mVoters = findViewById(R.id.button_voters);
    Button mBallot = findViewById(R.id.button_ballot);
    Button mResult = findViewById(R.id.button_result);
    mBallot.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        displayElectionViewModel.getBallot(token, id);
      }
    });
    mVoters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        displayElectionViewModel.getVoter(token, id);
      }
    });
    mInviteVoters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (status.equals("Finished")) {
          Toast.makeText(ElectionActivity.this, "Election is Finished", Toast.LENGTH_SHORT).show();
        } else {
          Intent intent = new Intent(getApplicationContext(), InviteVotersActivity.class);
          intent.putExtra("id", id);
          intent.putExtra("token", token);
          startActivity(intent);
        }
      }
    });
    mResult.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (status.equals("Pending")) {
          Toast.makeText(ElectionActivity.this, "Election is not started yet", Toast.LENGTH_SHORT)
              .show();
        } else {
          resultViewModel.getResult(token, id);
        }
      }
    });

    mDeleteElection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        switch (status) {
          case "Active":
            Toast.makeText(ElectionActivity.this, "Active Elections Cannot Be Deleted",
                Toast.LENGTH_SHORT).show();
            break;
          case "Finished":
            displayElectionViewModel.deleteElection(token, id);
            break;
          case "Pending":
            displayElectionViewModel.deleteElection(token, id);
            break;
        }
      }
    });

    getIncomingIntent();
  }

  private void getIncomingIntent() {

    if (getIntent().hasExtra("election_name") && getIntent().hasExtra("election_description")
        && getIntent().hasExtra("start_date") && getIntent().hasExtra("end_date")
        && getIntent().hasExtra("candidates") && getIntent().hasExtra("status")) {

      String electionName = getIntent().getStringExtra("election_name");
      String electionDescription = getIntent().getStringExtra("election_description");
      String startDate = getIntent().getStringExtra("start_date");
      String endDate = getIntent().getStringExtra("end_date");
      String candidateName = getIntent().getStringExtra("candidates");
      status = getIntent().getStringExtra("status");
      id = getIntent().getStringExtra("id");

      switch (status) {
        case "Active":
          constraintLayout.setBackgroundColor(Color.rgb(226, 11, 11));
          break;
        case "Finished":
          constraintLayout.setBackgroundColor(Color.rgb(5, 176, 197));
          break;
        case "Pending":
          constraintLayout.setBackgroundColor(Color.rgb(75, 166, 79));
          break;
      }
      mElectionName.setText(electionName);
      mElectionDescription.setText(electionDescription);
      mStartDate.setText(startDate);
      mEndDate.setText(endDate);
      mCandidateName.setText(candidateName);
      mStatus.setText(status);
    }
  }
}
