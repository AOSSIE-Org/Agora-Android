package org.aossie.agoraandroid.ui.fragments.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_upload_election.view.invite_voters_check
import kotlinx.android.synthetic.main.fragment_upload_election.view.progress_bar
import kotlinx.android.synthetic.main.fragment_upload_election.view.radioGroup
import kotlinx.android.synthetic.main.fragment_upload_election.view.radioGroup2
import kotlinx.android.synthetic.main.fragment_upload_election.view.real_time_results_check
import kotlinx.android.synthetic.main.fragment_upload_election.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UploadElectionFragment
  @Inject
  constructor(
    private val viewModelFactory: ViewModelProvider.Factory,
      private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs,
      private val prefs: PreferenceProvider
  ): Fragment(), CreateElectionListener {

  private lateinit var rootView: View

  private val createElectionViewModel: CreateElectionViewModel by viewModels {
    viewModelFactory
  }
  private var mFinalIsInvite: Boolean? = null
  private var mFinalIsRealTime: Boolean? = null
  private var voterListVisibility: Boolean? = null
  private var radioButtonListVoters: RadioButton? = null
  private var radioButtonBallots: RadioButton? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_upload_election, container, false)

    createElectionViewModel.createElectionListener = this

    rootView.submit_details_btn.setOnClickListener {
      val radioId = rootView.radioGroup2.checkedRadioButtonId
      radioButtonListVoters = rootView.findViewById(radioId)
      val radioId1 = rootView.radioGroup.checkedRadioButtonId
      radioButtonBallots = rootView.findViewById(radioId1)
      voterListVisibility = radioButtonListVoters!!.text
          .toString() != "Only me"
      mFinalIsInvite = rootView.invite_voters_check.isChecked
      mFinalIsRealTime = rootView.real_time_results_check.isChecked
      electionDetailsSharedPrefs.saveIsInvite(mFinalIsInvite)
      electionDetailsSharedPrefs.saveIsRealTime(mFinalIsRealTime)
      electionDetailsSharedPrefs.saveVoterListVisibility(voterListVisibility)
      electionDetailsSharedPrefs.saveBallotVisibility(
          radioButtonBallots!!.text
              .toString()
              .trim { it <= ' ' }
      )
      createElectionViewModel.createElection()
    }

    return rootView
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onSuccess(message: String?) {
    rootView.progress_bar.hide()
    if(message!=null) rootView.snackbar(message)
    prefs.setUpdateNeeded(true)
    electionDetailsSharedPrefs.clearElectionData()
    Navigation.findNavController(rootView)
        .navigate(UploadElectionFragmentDirections.actionUploadElectionFragmentToHomeFragment())
  }

  override fun onFailure(message: String) {
    rootView.progress_bar.hide()
    rootView.snackbar(message)
  }

}
