package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_finished_elections.view.rv_finished_elections
import kotlinx.android.synthetic.main.fragment_finished_elections.view.tv_empty_election
import kotlinx.android.synthetic.main.fragment_finished_elections.view.tv_something_went_wrong
import org.aossie.agoraandroid.ElectionAdapterCallback
import org.aossie.agoraandroid.ElectionRecyclerAdapterCallback
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.adapters.ElectionsRecyclerAdapter
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.show
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FinishedElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
): Fragment(), ElectionRecyclerAdapterCallback {

  private lateinit var rootView: View

  private val displayElectionViewModel: DisplayElectionViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mElections: ArrayList<Election>
  private lateinit var electionsAdapter: ElectionsAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_finished_elections, container, false)
    mElections = ArrayList()
    electionsAdapter = ElectionsAdapter(mElections as List<Election>, this)
    rootView.rv_finished_elections.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = electionsAdapter
    }
//    if(rootView == null) {
//      rootView = inflater.inflate(R.layout.fragment_finished_elections, container, false)
//
//      val electionDetailsSharedPrefs =
//        ElectionDetailsSharedPrefs(context!!)
//      val mLayoutManager: LayoutManager = LinearLayoutManager(context)
//      rootView?.rv_finished_elections?.layoutManager = mLayoutManager
//      val electionsRecyclerAdapter = ElectionsRecyclerAdapter(
//          mIDList, context, mElectionNameList, mElectionDescriptionList,
//          mElectionStartDateList, mElectionEndDateList, mElectionStatusList, mCandidatesList,
//          "finished", this
//      )
//      rootView?.rv_finished_elections?.adapter = electionsRecyclerAdapter
//
//      try {
//        val jsonObject =
//          JSONObject(electionDetailsSharedPrefs.electionDetails)
//        val electionsJsonArray = jsonObject.getJSONArray("elections")
//        Log.d("friday", electionsJsonArray.toString())
//        for (i in 0 until electionsJsonArray.length()) {
//          val mCandidateName = StringBuilder()
//          val singleElectionJsonObject = electionsJsonArray.getJSONObject(i)
//          val formatter =
//            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//          val formattedStartingDate =
//            formatter.parse(singleElectionJsonObject.getString("start"))
//          val formattedEndingDate =
//            formatter.parse(singleElectionJsonObject.getString("end"))
//          val currentDate = Calendar.getInstance()
//              .time
//          if (currentDate.after(formattedEndingDate)) {
//            mElectionStatusList.add("Finished")
//            mElectionStartDateList.add(formattedStartingDate.toString())
//            mElectionEndDateList.add(formattedEndingDate.toString())
//            mElectionNameList.add(singleElectionJsonObject.getString("name"))
//            mIDList.add(singleElectionJsonObject.getString("_id"))
//            mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))
//          }
//          val candidatesJsonArray =
//            singleElectionJsonObject.getJSONArray("candidates")
//          for (j in 0 until candidatesJsonArray.length()) {
//            mCandidateName.append(candidatesJsonArray.getString(j))
//                .append("\n")
//          }
//          mCandidatesList.add(
//              mCandidateName.toString()
//                  .trim { it <= ' ' }
//          )
//        }
//      } catch (e: JSONException) {
//        e.printStackTrace()
//      } catch (e: ParseException) {
//        e.printStackTrace()
//      }
//    }


    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    bindUI()
  }

  private fun bindUI() {
    Coroutines.main {
      try {
        val elections = displayElectionViewModel.finishedElections.await()
        elections.observe(requireActivity(), Observer {
          if (it != null) {
            addElections(it)
          }
        })
      } catch (e: IllegalStateException) {
        rootView.tv_something_went_wrong.show()
      }
    }
  }

  private fun addElections(elections: List<Election>) {
    if (elections.isNotEmpty()) {
      mElections.addAll(elections)
      electionsAdapter.notifyDataSetChanged()
    } else {
      rootView.tv_empty_election.show()
    }
  }

  override fun onItemClicked(id: String) {
    val action =
      FinishedElectionsFragmentDirections.actionFinishedElectionsFragmentToElectionDetailsFragment(id)
    Navigation.findNavController(rootView!!)
        .navigate(action)
  }

}
