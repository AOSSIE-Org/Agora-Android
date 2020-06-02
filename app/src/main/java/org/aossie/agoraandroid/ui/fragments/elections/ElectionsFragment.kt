package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import kotlinx.android.synthetic.main.fragment_elections.view.rv_total_elections
import org.aossie.agoraandroid.ElectionAdapterCallback
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.ElectionsRecyclerAdapter
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 */
class ElectionsFragment : Fragment(), ElectionAdapterCallback {

  private var rootView: View? = null

  private val mElectionNameList =
    ArrayList<String>()
  private val mElectionDescriptionList =
    ArrayList<String>()
  private val mElectionStartDateList =
    ArrayList<String>()
  private val mElectionEndDateList =
    ArrayList<String>()
  private val mElectionStatusList =
    ArrayList<String>()
  private val mCandidatesList =
    ArrayList<String>()
  private val mIDList = ArrayList<String>()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    if(rootView == null) {
      rootView = inflater.inflate(R.layout.fragment_elections, container, false)

      val electionDetailsSharedPrefs =
        ElectionDetailsSharedPrefs(context!!)
      val mLayoutManager: LayoutManager = LinearLayoutManager(context)
      rootView?.rv_total_elections?.layoutManager = mLayoutManager
      try {
        val jsonObject =
          JSONObject(electionDetailsSharedPrefs.electionDetails)
        val electionsJsonArray = jsonObject.getJSONArray("elections")
        for (i in 0 until electionsJsonArray.length()) {
          val mCandidateName = StringBuilder()
          val singleElectionJsonObject = electionsJsonArray.getJSONObject(i)
          mElectionNameList.add(singleElectionJsonObject.getString("name"))
          mIDList.add(singleElectionJsonObject.getString("_id"))
          mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))
          val formatter =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
          val formattedStartingDate =
            formatter.parse(singleElectionJsonObject.getString("start"))
          val formattedEndingDate =
            formatter.parse(singleElectionJsonObject.getString("end"))
          mElectionStartDateList.add(formattedStartingDate.toString())
          mElectionEndDateList.add(formattedEndingDate.toString())
          val currentDate = Calendar.getInstance()
              .time
          if (currentDate.before(formattedStartingDate)) {
            mElectionStatusList.add("Pending")
          } else if (currentDate.after(formattedStartingDate) && currentDate.before(
                  formattedEndingDate
              )
          ) {
            mElectionStatusList.add("Active")
          } else if (currentDate.after(formattedEndingDate)) {
            mElectionStatusList.add("Finished")
          }
          val candidatesJsonArray =
            singleElectionJsonObject.getJSONArray("candidates")
          for (j in 0 until candidatesJsonArray.length()) {
            mCandidateName.append(candidatesJsonArray.getString(j))
                .append("\n")
          }
          mCandidatesList.add(
              mCandidateName.toString()
                  .trim { it <= ' ' }
          )
        }
      } catch (e: JSONException) {
        e.printStackTrace()
      } catch (e: ParseException) {
        e.printStackTrace()
      }

      val electionsRecyclerAdapter = ElectionsRecyclerAdapter(
          mIDList, context, mElectionNameList, mElectionDescriptionList,
          mElectionStartDateList, mElectionEndDateList, mElectionStatusList, mCandidatesList,
          "total", this
      )
      rootView?.rv_total_elections?.adapter = electionsRecyclerAdapter
    }
    return rootView
  }

  override fun onItemClicked(
    electionName: String,
    electionDesc: String,
    startDate: String,
    endDate: String,
    status: String,
    candidate: String,
    id: String
  ) {
    val action = ElectionsFragmentDirections
        .actionElectionsFragmentToElectionDetailsFragment(
            electionName,
            electionDesc,
            startDate,
            endDate,
            status,
            candidate,
            id
        )
    Navigation.findNavController(rootView!!)
        .navigate(action)
  }

}
