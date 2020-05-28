package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_voters.view.recycler_view_voters
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.VotersRecyclerAdapter
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class VotersFragment : Fragment() {

  private lateinit var rootView: View

  private val mVoterEmailList =
    ArrayList<String>()
  private val mVoterNameList =
    ArrayList<String>()
  private var voterResponse: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_voters, container, false)
    val mLayoutManager: LayoutManager = LinearLayoutManager(context)
    rootView.recycler_view_voters.layoutManager = mLayoutManager

    voterResponse = VotersFragmentArgs.fromBundle(arguments!!).voterResponse
    try {
      val jsonObject = JSONObject(voterResponse!!)
      val ballotJsonArray = jsonObject.getJSONArray("voters")
      for (i in 0 until ballotJsonArray.length()) {
        val ballotJsonObject = ballotJsonArray.getJSONObject(i)
        mVoterEmailList.add(ballotJsonObject.getString("hash"))
        mVoterNameList.add(ballotJsonObject.getString("name"))
      }
    } catch (e: JSONException) {
      e.printStackTrace()
    }

    val votersRecyclerAdapter =
      VotersRecyclerAdapter(mVoterNameList, mVoterEmailList)
    rootView.recycler_view_voters.adapter = votersRecyclerAdapter

    return rootView
  }

}
