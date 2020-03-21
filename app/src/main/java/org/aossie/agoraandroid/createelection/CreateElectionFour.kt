package org.aossie.agoraandroid.createelection

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import org.aossie.agoraandroid.R

class CreateElectionFour : Fragment() {
private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
private var createElectionFourViewModel: CreateElectionFourViewModel? = null
private var isInvite: CheckBox? = null
private var isRealTime: CheckBox? = null
private var mFinalIsInvite: Boolean? = null
private var mFinalIsRealTime: Boolean? = null
private var voterListVisibility: Boolean? = null
private var radioButtonListVoters: RadioButton? = null
private var radioButtonBallots: RadioButton? = null
private var radioGroupListVoters: RadioGroup? = null
private var radioGroupBallots: RadioGroup? = null
    @RequiresApi(VERSION_CODES.KITKAT)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? { // Inflate the layout for this fragment
    val view: View = inflater.inflate(R.layout.create_election_four_fragment, container, false)
    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(activity)
    createElectionFourViewModel = CreateElectionFourViewModel(activity!!.application, context)
    val mSubmitButton = view.findViewById<Button>(R.id.submit_details_btn)
    isInvite = view.findViewById(R.id.invite_voters_check)
    isRealTime = view.findViewById(R.id.real_time_results_check)
    radioGroupListVoters = view.findViewById(R.id.radioGroup2)
    radioGroupBallots = view.findViewById(R.id.radioGroup)
    mSubmitButton.setOnClickListener {
    val radioId = radioGroupListVoters!!.getCheckedRadioButtonId()
    radioButtonListVoters = view.findViewById(radioId)
    val radioId1 = radioGroupBallots!!.getCheckedRadioButtonId()
    radioButtonBallots = view.findViewById(radioId1)
    voterListVisibility = radioButtonListVoters!!.getText().toString() != "Only me"
    mFinalIsInvite = isInvite!!.isChecked()
    mFinalIsRealTime = isRealTime!!.isChecked()
    electionDetailsSharedPrefs!!.saveIsInvite(mFinalIsInvite)
    electionDetailsSharedPrefs!!.saveIsRealTime(mFinalIsRealTime)
    electionDetailsSharedPrefs!!.saveVoterListVisibility(voterListVisibility)
    electionDetailsSharedPrefs!!.saveBallotVisibility(
    radioButtonBallots!!.getText().toString().trim { it <= ' ' })
    createElectionFourViewModel!!.createElection()
    }
    return view
    }
    }