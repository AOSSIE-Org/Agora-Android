package org.aossie.agoraandroid.ui.fragments.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_upload_voting_algo.view.radioGroup
import kotlinx.android.synthetic.main.fragment_upload_voting_algo.view.submit_details_btn
import org.aossie.agoraandroid.R
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UploadVotingAlgoFragment
  @Inject
  constructor(
    private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs
  ): Fragment() {

  private lateinit var rootView: View
  private var votingAlgorithm: String? = null
  private var radioGroup: RadioGroup? = null
  private var radioButton: RadioButton? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_upload_voting_algo, container, false)

    rootView.submit_details_btn.setOnClickListener {
      val radioId = rootView.radioGroup.checkedRadioButtonId
      radioButton = rootView.findViewById(radioId)
      votingAlgorithm = radioButton!!.text
          .toString()
      electionDetailsSharedPrefs.saveVotingAlgo(votingAlgorithm)
      Navigation.findNavController(rootView)
          .navigate(UploadVotingAlgoFragmentDirections.actionUploadVotingAlgoFragmentToUploadElectionFragment())
    }

    return rootView
  }

}
