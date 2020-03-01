package org.aossie.agoraandroid.createelection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.createelection.CreateElectionThree

class CreateElectionThree : AppCompatActivity() {
    private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
    private var votingAlgorithm: String? = null
    private var radioGroup: RadioGroup ? = null
    private var radioButton: RadioButton? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_election_three)
        electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(application)
        val mFinalStepButton = findViewById<Button>(R.id.submit_details_btn)
        radioGroup = findViewById(R.id.radioGroup)
        mFinalStepButton.setOnClickListener {

          val radioId = radioGroup?.checkedRadioButtonId
            radioButton = findViewById(radioId!!)
            votingAlgorithm = radioButton?.getText().toString()
            electionDetailsSharedPrefs!!.saveVotingAlgo(votingAlgorithm)
            startActivity(Intent(this@CreateElectionThree, CreateElectionFour::class.java))

        }
    }
}