package org.aossie.agoraandroid.ui.fragments.displayelections

import android.content.Intent
import android.content.Intent.getIntent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_election_details.view.button_ballot
import kotlinx.android.synthetic.main.fragment_election_details.view.button_delete
import kotlinx.android.synthetic.main.fragment_election_details.view.button_invite_voters
import kotlinx.android.synthetic.main.fragment_election_details.view.button_result
import kotlinx.android.synthetic.main.fragment_election_details.view.button_voters
import kotlinx.android.synthetic.main.fragment_election_details.view.constraint_layout
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_candidate_list
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_description
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_election_name
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_end_date
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_start_date
import kotlinx.android.synthetic.main.fragment_election_details.view.tv_status
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.invitevoters.InviteVotersActivity
import org.aossie.agoraandroid.result.ResultViewModel
import org.aossie.agoraandroid.ui.fragments.elections.ElectionsFragmentDirections
import org.aossie.agoraandroid.utilities.SharedPrefs

/**
 * A simple [Fragment] subclass.
 */
class ElectionDetailsFragment : Fragment(), DisplayElectionListener {

  private lateinit var rootView: View
  private var id: String? = null
  private var status: String? = null
  private var token: String? = null
  private var displayElectionViewModel: DisplayElectionViewModel? = null
  private var resultViewModel: ResultViewModel? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_election_details, container, false)

    val sharedPrefs = SharedPrefs(context!!)
    displayElectionViewModel = DisplayElectionViewModel(activity!!.application, context!!)
    displayElectionViewModel?.displayElectionListener = this
    resultViewModel = ResultViewModel(activity!!.application, context)
    token = sharedPrefs.token
    rootView.button_ballot.setOnClickListener { displayElectionViewModel?.getBallot(token, id) }
    rootView.button_voters.setOnClickListener { displayElectionViewModel?.getVoter(token, id) }
    rootView.button_invite_voters.setOnClickListener {
      if (status == "Finished") {
        Toast.makeText(context, "Election is Finished", Toast.LENGTH_SHORT)
            .show()
      } else {
        val intent = Intent(
            context,
            InviteVotersActivity::class.java
        )
        intent.putExtra("id", id)
        intent.putExtra("token", token)
        startActivity(intent)
      }
    }
    rootView.button_result.setOnClickListener {
      if (status == "Pending") {
        Toast.makeText(context, "Election is not started yet", Toast.LENGTH_SHORT)
            .show()
      } else {
        resultViewModel?.getResult(token, id)
      }
    }

    rootView.button_delete.setOnClickListener {
      when (status) {
        "Active" -> Toast.makeText(
            context, "Active Elections Cannot Be Deleted",
            Toast.LENGTH_SHORT
        )
            .show()
        "Finished" -> displayElectionViewModel?.deleteElection(token, id)
        "Pending" -> displayElectionViewModel?.deleteElection(token, id)
      }
    }

    displayElectionViewModel!!.voterResponse.observe(
        viewLifecycleOwner, Observer {
      Log.d("friday", it)
      val action = ElectionDetailsFragmentDirections
          .actionElectionDetailsFragmentToVotersFragment(it!!)
      Navigation.findNavController(rootView).navigate(action)
    })

    displayElectionViewModel!!.ballotResponse.observe(
        viewLifecycleOwner, Observer {
      val action = ElectionDetailsFragmentDirections
          .actionElectionDetailsFragmentToBallotFragment(it!!)
      Navigation.findNavController(rootView).navigate(action)
    })

    getIncomingIntent()
    return rootView
  }

  private fun getIncomingIntent() {
    val args = ElectionDetailsFragmentArgs.fromBundle(arguments!!)
    val electionName: String = args.electionName
    val electionDesc: String = args.electionDesc
    val startDate: String = args.startDate
    val endDate: String = args.endDate
    val candidate: String = args.candidate
    status = args.status
    id = args.id
    Log.d("friday : ", status)
    when (status) {
      "Active" -> rootView.constraint_layout.setBackgroundColor(Color.rgb(226, 11, 11))
      "Finished" -> rootView.constraint_layout.setBackgroundColor(Color.rgb(5, 176, 197))
      "Pending" -> rootView.constraint_layout.setBackgroundColor(Color.rgb(75, 166, 79))
    }
    rootView.tv_election_name.text = electionName
    rootView.tv_description.text = electionDesc
    rootView.tv_start_date.text = startDate
    rootView.tv_end_date.text = endDate
    rootView.tv_candidate_list.text = candidate
    rootView.tv_status.text = status
  }

  override fun onDeleteElectionSuccess() {
    Navigation.findNavController(rootView)
        .navigate(ElectionDetailsFragmentDirections.actionElectionDetailsFragmentToHomeFragment())
  }
}


