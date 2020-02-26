package org.aossie.agoraandroid.createelection

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import org.aossie.agoraandroid.R

class CreateElectionFour : AppCompatActivity() {
    private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
    private var createElectionViewModel: CreateElectionViewModel? = null
    private var isInvite: CheckBox? = null
    private var isRealTime: CheckBox? = null
    private var mFinalIsInvite: Boolean? = null
    private var mFinalIsRealTime: Boolean? = null
    private var voterListVisibility: Boolean? = null
    private var radioButtonListVoters: RadioButton? = null
    private var radioButtonBallots: RadioButton? = null
    private var radioGroupListVoters: RadioGroup? = null
    private var radioGroupBallots: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(application)
        createElectionViewModel = CreateElectionViewModel(application, this)
        setContentView(R.layout.activity_create_election_four)
        val mSubmitButton = findViewById<Button>(R.id.submit_details_btn)
        isInvite = findViewById(R.id.invite_voters_check)
        isRealTime = findViewById(R.id.real_time_results_check)
        radioGroupListVoters = findViewById(R.id.radioGroup2)
        radioGroupBallots = findViewById(R.id.radioGroup)
                /*   mSubmitButton.setOnClickListener {
            val radioId = radioGroupListVoters.getCheckedRadioButtonId()
            radioButtonListVoters = findViewById(radioId)
            val radioId1 = radioGroupBallots.getCheckedRadioButtonId()
            radioButtonBallots = findViewById(radioId1)
            voterListVisibility = radioButtonListVoters.getText().toString() != "Only me"
            mFinalIsInvite = isInvite.isChecked()
            mFinalIsRealTime = isRealTime.isChecked()
            electionDetailsSharedPrefs!!.saveIsInvite(mFinalIsInvite)
            electionDetailsSharedPrefs!!.saveIsRealTime(mFinalIsRealTime)
            electionDetailsSharedPrefs!!.saveVoterListVisibility(voterListVisibility)
            electionDetailsSharedPrefs!!.saveBallotVisibility(
                    radioButtonBallots.getText().toString().trim { it <= ' ' })
            createElectionViewModel!!.createElection()
        }*/
    }
}