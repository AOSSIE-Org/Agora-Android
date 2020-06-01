package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_ballot.view.recycler_view_ballots
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.BallotRecyclerAdapter
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment : Fragment() {

  private val mVoterEmailList =
    ArrayList<String>()
  private val mVoteBallotList =
    ArrayList<String>()
  private var ballotResponse: String? = null

  private lateinit var rootView: View

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_ballot, container, false)

    val mLayoutManager: LayoutManager = LinearLayoutManager(context)
    rootView.recycler_view_ballots.layoutManager = mLayoutManager

    ballotResponse = BallotFragmentArgs.fromBundle(arguments!!).ballotResponse

    try {
      val jsonObject = JSONObject(ballotResponse)
      val ballotJsonArray = jsonObject.getJSONArray("ballots")
      for (i in 0 until ballotJsonArray.length()) {
        val ballotJsonObject = ballotJsonArray.getJSONObject(i)
        mVoterEmailList.add(ballotJsonObject.getString("voterEmail"))
        mVoteBallotList.add(ballotJsonObject.getString("voteBallot"))
      }
    } catch (e: JSONException) {
      e.printStackTrace()
    }

    val ballotRecyclerAdapter =
      BallotRecyclerAdapter(mVoterEmailList, mVoteBallotList)
    rootView.recycler_view_ballots.adapter = ballotRecyclerAdapter

    return rootView
  }

}
