package org.aossie.agoraandroid.createelection

import android.os.Build.VERSION_CODES
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_new_election.new_election_step_tv
import kotlinx.android.synthetic.main.activity_new_election.new_election_vp
import kotlinx.android.synthetic.main.activity_new_election.step_title_tv
import kotlinx.android.synthetic.main.activity_new_election.toolbar_new_election
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.NewElectionAdapter

class NewElectionActivity : AppCompatActivity() {

  private val titles = arrayOf(
      "Election info",
      "Candidates",
      "Voting algorithm",
      "Additional settings"
  )

  var mElectionName: String? = null
  var mElectionDescription: String? = null
  var mStartDate: String? = null
  var mEndDate: String? = null

  val mCandidateList = ArrayList<String>()

  var mSelectedVotingAlgorithm = ""

  var mFinalIsInvite: Boolean = false
  var mFinalIsRealTime: Boolean = false
  var mVoterListVisibility: Boolean = false
  var mBallot: String = ""

  private lateinit var mElectionModel: CreateElectionViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_new_election)
    mElectionModel = CreateElectionViewModel(application, this)
    new_election_vp.adapter = NewElectionAdapter(supportFragmentManager, this, 4)
    step_title_tv.text = titles[new_election_vp.currentItem]
    new_election_step_tv.text = "${new_election_vp.currentItem + 1}/4"
    setSupportActionBar(toolbar_new_election)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  override fun onBackPressed() {
    if(new_election_vp.currentItem == 0) finish()
    else prevStep()
  }

  override fun onSupportNavigateUp(): Boolean {
    if(new_election_vp.currentItem == 0) finish()
    else prevStep()
    return true
  }

  fun nextStep() {
    new_election_vp.currentItem = new_election_vp.currentItem + 1
    step_title_tv.text = titles[new_election_vp.currentItem]
    new_election_step_tv.text = "${new_election_vp.currentItem + 1}/4"
  }

  private fun prevStep() {
    new_election_vp.currentItem = new_election_vp.currentItem - 1
    step_title_tv.text = titles[new_election_vp.currentItem]
    new_election_step_tv.text = "${new_election_vp.currentItem + 1}/4"
  }

  @RequiresApi(VERSION_CODES.KITKAT) fun saveElection() {
    mElectionModel.createElection(
        mElectionName,
        mElectionDescription,
        mStartDate,
        mEndDate,
        mCandidateList,
        mSelectedVotingAlgorithm,
        mFinalIsInvite,
        mFinalIsRealTime,
        mVoterListVisibility,
        mBallot
    )
  }

}
