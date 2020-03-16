package org.aossie.agoraandroid.createelection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import kotlinx.android.synthetic.main.fragment_new_election_three.*

import org.aossie.agoraandroid.R

class NewElectionThreeFragment(private val callingActivity: NewElectionActivity) : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_new_election_three, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    submit_details_btn.setOnClickListener {
      callingActivity.mSelectedVotingAlgorithm = activity?.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)?.text.toString()
      callingActivity.nextStep()
    }
  }

}
