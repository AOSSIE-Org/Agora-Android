package org.aossie.agoraandroid.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.aossie.agoraandroid.R

class CreateElectionThree : Fragment() {
    private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
    private var votingAlgorithm: String? = null
    private var radioGroup: RadioGroup? = null
    private var radioButton: RadioButton? = null
    private var radioId: Int? = null




    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_election_three_fragment, container, false)
        electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(context!!)
        val mFinalStepButton: Button? = view?.findViewById(R.id.submit_details_btn)
        radioGroup = view?.findViewById(R.id.radioGroup)
        mFinalStepButton?.setOnClickListener(View.OnClickListener {
            radioId = radioGroup?.checkedRadioButtonId
            radioButton = view.findViewById<RadioButton>(radioId!!)
            votingAlgorithm = radioButton?.text.toString()
            electionDetailsSharedPrefs?.saveVotingAlgo(votingAlgorithm!!)
            Navigation.findNavController(it).navigate(R.id.action_election3_to_election4)
        })
        return view
    }


}
