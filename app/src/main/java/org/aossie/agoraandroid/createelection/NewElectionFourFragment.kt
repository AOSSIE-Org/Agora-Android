package org.aossie.agoraandroid.createelection

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_new_election_four.*
import org.aossie.agoraandroid.R

class NewElectionFourFragment(private val callingActivity: NewElectionActivity) : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_new_election_four, container, false)
  }


  @RequiresApi(VERSION_CODES.KITKAT)
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    radioGroup.check(R.id.ballot_visibility_secret_radio_btn)
    radioGroup.setOnCheckedChangeListener { _, checkedId ->
      callingActivity.mBallot = activity?.findViewById<RadioButton>(checkedId)?.text.toString().trim()
    }

    radioGroup2.check(R.id.access_only_me_radio_btn)
    radioGroup2.setOnCheckedChangeListener { _, checkedId ->
      callingActivity.mVoterListVisibility = when(checkedId) {
        R.id.radioGroup2 -> false
        else -> true
      }
    }

    invite_voters_check.setOnCheckedChangeListener { _, isChecked ->
      callingActivity.mFinalIsInvite = isChecked
    }

    real_time_results_check.setOnCheckedChangeListener { _, isChecked ->
      callingActivity.mFinalIsRealTime = isChecked
    }

    submit_details_btn.setOnClickListener {
      callingActivity.saveElection()
    }
  }

}
