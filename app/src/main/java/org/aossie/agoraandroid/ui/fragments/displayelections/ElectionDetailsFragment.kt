package org.aossie.agoraandroid.ui.fragments.displayelections

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_election_details.view.button_ballot
import kotlinx.android.synthetic.main.fragment_election_details.view.button_delete
import kotlinx.android.synthetic.main.fragment_election_details.view.button_invite_voters
import kotlinx.android.synthetic.main.fragment_election_details.view.button_result
import kotlinx.android.synthetic.main.fragment_election_details.view.button_voters
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.invitevoters.InviteVotersActivity
import org.aossie.agoraandroid.result.ResultViewModel
import org.aossie.agoraandroid.utilities.SharedPrefs

/**
 * A simple [Fragment] subclass.
 */
class ElectionDetailsFragment : Fragment() {

  private lateinit var rootView: View
  private var id: String? = null
  private  var status: String? = null
  private  var token: String? = null
  private var displayElectionViewModel: DisplayElectionViewModel? = null
  private var resultViewModel: ResultViewModel? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(R.layout.fragment_election_details, container, false)

    val sharedPrefs = SharedPrefs(context!!)
    displayElectionViewModel = DisplayElectionViewModel(activity!!.application, context)
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

    //getIncomingIntent()

    return rootView
  }
}
