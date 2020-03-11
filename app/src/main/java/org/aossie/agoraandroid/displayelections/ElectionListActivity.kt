package org.aossie.agoraandroid.displayelections

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.adapters.ElectionsRecyclerAdapter
import org.aossie.agoraandroid.createelection.ElectionDetailsSharedPrefs
import org.json.JSONException
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

private const val TYPE_ACTIVE = 0
private const val TYPE_PENDING = 1
private const val TYPE_FINISHED = 2
private const val TYPE_TOTAL = 3

class ElectionListActivity : AppCompatActivity() {

  private var electionType = TYPE_TOTAL
  private val toolbarTitles = arrayOf("Active elections", "Pending elections", "Finished elections", "Total elections")

  private val mElectionNameList = ArrayList<String>()
  private val mElectionDescriptionList = ArrayList<String>()
  private val mElectionStartDateList = ArrayList<String>()
  private val mElectionEndDateList = ArrayList<String>()
  private val mElectionStatusList = ArrayList<String>()
  private val mCandidatesList = ArrayList<String>()
  private val mIDList = ArrayList<String>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_election_list)
    electionType = intent.getIntExtra("electionType", TYPE_TOTAL)
    //added back button to Toolbar
    val toolbar =
      findViewById<Toolbar>(id.toolbar)
    setSupportActionBar(toolbar)
    supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    supportActionBar!!.setDisplayShowHomeEnabled(true)
    toolbar?.setNavigationOnClickListener { onBackPressed() }
    title = toolbarTitles[electionType]
    val electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(applicationContext)
    val rvElectionDetails = findViewById<RecyclerView>(id.rv_active_elections)

    val mLayoutManager: LayoutManager = LinearLayoutManager(applicationContext)
    rvElectionDetails.layoutManager = mLayoutManager
    try {
      val jsonObject = JSONObject(electionDetailsSharedPrefs.electionDetails)
      val electionsJsonArray = jsonObject.getJSONArray("elections")
      for (i in 0 until electionsJsonArray.length()) {
        val mCandidateName = StringBuilder()
        val singleElectionJsonObject = electionsJsonArray.getJSONObject(i)

        when(electionType) {
          TYPE_ACTIVE -> {
            loadActiveElections(singleElectionJsonObject)
          }
          TYPE_PENDING -> {
            loadPendingElections(singleElectionJsonObject)
          }
          TYPE_FINISHED -> {
            loadFinishedElections(singleElectionJsonObject)
          }
          else -> {
            loadTotalElections(singleElectionJsonObject)
          }
        }

        val candidatesJsonArray = singleElectionJsonObject.getJSONArray("candidates")
        for (j in 0 until candidatesJsonArray.length()) {
          mCandidateName.append(candidatesJsonArray.getString(j)).append("\n")
        }
        mCandidatesList.add(mCandidateName.toString().trim { it <= ' ' })
      }
    } catch (e: JSONException) {
      e.printStackTrace()
    } catch (e: ParseException) {
      e.printStackTrace()
    }

    val electionsRecyclerAdapter = ElectionsRecyclerAdapter(
        mIDList, this, mElectionNameList, mElectionDescriptionList,
        mElectionStartDateList, mElectionEndDateList, mElectionStatusList, mCandidatesList,
        "active"
    )
    rvElectionDetails.adapter = electionsRecyclerAdapter
  }

  private fun loadTotalElections(singleElectionJsonObject: JSONObject?) {
    mElectionNameList.add(singleElectionJsonObject!!.getString("name"))
    mIDList.add(singleElectionJsonObject.getString("_id"))
    mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))

    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedStartingDate = formatter.parse(singleElectionJsonObject.getString("start"))
    val formattedEndingDate = formatter.parse(singleElectionJsonObject.getString("end"))
    mElectionStartDateList.add(formattedStartingDate.toString())
    mElectionEndDateList.add(formattedEndingDate.toString())
    val currentDate = Calendar.getInstance().time
    if (currentDate.before(formattedStartingDate)) {
      mElectionStatusList.add("Pending")
    } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
      mElectionStatusList.add("Active")
    } else if (currentDate.after(formattedEndingDate)) {
      mElectionStatusList.add("Finished")
    }
  }

  private fun loadFinishedElections(singleElectionJsonObject: JSONObject?) {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedStartingDate = formatter.parse(singleElectionJsonObject!!.getString("start"))
    val formattedEndingDate = formatter.parse(singleElectionJsonObject.getString("end"))
    val currentDate = Calendar.getInstance().time

    if (currentDate.after(formattedEndingDate)) {
      mElectionStatusList.add("Finished")
      mElectionStartDateList.add(formattedStartingDate.toString())
      mElectionEndDateList.add(formattedEndingDate.toString())
      mElectionNameList.add(singleElectionJsonObject.getString("name"))
      mIDList.add(singleElectionJsonObject.getString("_id"))
      mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))
    }
  }

  private fun loadPendingElections(singleElectionJsonObject: JSONObject?) {
    val formatter =SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedStartingDate = formatter.parse(singleElectionJsonObject!!.getString("start"))
    val formattedEndingDate = formatter.parse(singleElectionJsonObject.getString("end"))
    val currentDate = Calendar.getInstance().time

    if (currentDate.before(formattedStartingDate)) {
      mElectionStatusList.add("Pending")
      mElectionStartDateList.add(formattedStartingDate.toString())
      mElectionEndDateList.add(formattedEndingDate.toString())
      mElectionNameList.add(singleElectionJsonObject.getString("name"))
      mIDList.add(singleElectionJsonObject.getString("_id"))
      mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))
    }
  }

  private fun loadActiveElections(singleElectionJsonObject: JSONObject?) {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val formattedStartingDate =formatter.parse(singleElectionJsonObject!!.getString("start"))
    val formattedEndingDate = formatter.parse(singleElectionJsonObject.getString("end"))
    val currentDate = Calendar.getInstance().time
    if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
      mElectionStatusList.add("Active")
      mElectionStartDateList.add(formattedStartingDate.toString())
      mElectionEndDateList.add(formattedEndingDate.toString())
      mElectionNameList.add(singleElectionJsonObject.getString("name"))
      mIDList.add(singleElectionJsonObject.getString("_id"))
      mElectionDescriptionList.add(singleElectionJsonObject.getString("description"))
    }
  }
}
