package org.aossie.agoraandroid.createelection;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import org.aossie.agoraandroid.R;

public class CreateElectionFour extends Fragment {
  private ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
  private CreateElectionViewFourModel createElectionViewFourModel;
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    final View view = inflater.inflate(R.layout.create_election_four_fragment, container, false);

    electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getActivity());
    createElectionViewFourModel =
        new CreateElectionViewFourModel(getActivity().getApplication(), getContext());
    Button mSubmitButton = view.findViewById(R.id.submit_details_btn);
    isInvite = view.findViewById(R.id.invite_voters_check);
    isRealTime = view.findViewById(R.id.real_time_results_check);
    radioGroupListVoters = view.findViewById(R.id.radioGroup2);
    radioGroupBallots = view.findViewById(R.id.radioGroup);

    mSubmitButton.setOnClickListener(new View.OnClickListener() {
      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      @Override
      public void onClick(View v) {
        int radioId = radioGroupListVoters.getCheckedRadioButtonId();
        radioButtonListVoters = view.findViewById(radioId);

        int radioId1 = radioGroupBallots.getCheckedRadioButtonId();
        radioButtonBallots = view.findViewById(radioId1);

        voterListVisibility = !radioButtonListVoters.getText().toString().equals("Only me");

        mFinalIsInvite = isInvite.isChecked();
        mFinalIsRealTime = isRealTime.isChecked();
        electionDetailsSharedPrefs.saveIsInvite(mFinalIsInvite);
        electionDetailsSharedPrefs.saveIsRealTime(mFinalIsRealTime);
        electionDetailsSharedPrefs.saveVoterListVisibility(voterListVisibility);
        electionDetailsSharedPrefs.saveBallotVisibility(
            radioButtonBallots.getText().toString().trim());
        createElectionViewFourModel.createElection();
      }
    });

    return view;
  }
}
