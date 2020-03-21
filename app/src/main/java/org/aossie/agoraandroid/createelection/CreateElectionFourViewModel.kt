package org.aossie.agoraandroid.createelection

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.home.HomeActivity
import org.aossie.agoraandroid.remote.APIService
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.TinyDB
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

internal class CreateElectionFourViewModel(
  application: Application,
  private val context: Context?
) : AndroidViewModel(application) {
  private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs =
    ElectionDetailsSharedPrefs(getApplication<Application>())
  private val sharedPrefs: SharedPrefs = SharedPrefs(getApplication<Application>())
  private val tinydb: TinyDB = TinyDB(getApplication<Application>())
  private var loadToast: LoadToast? = null
  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  fun createElection() {
    loadToast = LoadToast(context)
    loadToast!!.setText("Creating Election")
    loadToast!!.show()
    val candidates: ArrayList<String?> = tinydb.getListString("Candidates")
    val jsArray = JSONArray(candidates)
    val token: String? = sharedPrefs.token
    val jsonObject = JSONObject()
    try {
      val map: MutableMap<String, String> = HashMap()
      map["voteBallot"] = ""
      map["hash"] = ""
      val ballot = JSONArray(arrayOf<Map<*, *>>(map))
      jsonObject.put("ballot", ballot) //Append the other JSONObject to the parent one
      jsonObject.put("name", electionDetailsSharedPrefs.getElectionName())
      jsonObject.put("description", electionDetailsSharedPrefs.getElectionDesc())
      jsonObject.put("voterListVisibility", electionDetailsSharedPrefs.getVoterListVisibility())
      jsonObject.put("startingDate", electionDetailsSharedPrefs.getStartTime())
      jsonObject.put("endingDate", electionDetailsSharedPrefs.getEndTime())
      jsonObject.put("isInvite", electionDetailsSharedPrefs.getIsInvite())
      jsonObject.put("ballotVisibility", electionDetailsSharedPrefs.getBallotVisibility())
      jsonObject.put("isRealTime", electionDetailsSharedPrefs.getIsRealTime())
      jsonObject.put("votingAlgo", electionDetailsSharedPrefs.getVotingAlgo())
      jsonObject.put("candidates", jsArray)
      jsonObject.put("noVacancies", 1)
      jsonObject.put("electionType", "Election")
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService: APIService = RetrofitClient.getAPIService()
    val electionResponse: Call<String> = apiService.createElection(jsonObject.toString(), token)
    electionResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>?,
        response: Response<String?>
      ) {
        if (response.message().equals("OK")) {
          loadToast!!.success()
          Toast.makeText(getApplication(), "Created Successfully", Toast.LENGTH_SHORT)
              .show()
          electionDetailsSharedPrefs.clearElectionData()
          val intent = Intent(context, HomeActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
          context!!.startActivity(intent)
        } else {
          loadToast!!.error()
          Toast.makeText(
              getApplication(), "Something went wrong please try again",
              Toast.LENGTH_SHORT
          )
              .show()
        }
      }

      override fun onFailure(
        call: Call<String?>?,
        t: Throwable?
      ) {
        loadToast!!.error()
        Toast.makeText(
            getApplication(), "Something went wrong please try again",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    })
  }

}