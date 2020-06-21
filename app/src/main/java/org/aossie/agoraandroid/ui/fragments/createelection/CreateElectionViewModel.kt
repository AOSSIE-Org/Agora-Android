package org.aossie.agoraandroid.ui.fragments.createelection

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION_CODES
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import net.steamcrafted.loadtoast.LoadToast
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.ui.fragments.moreOptions.HomeActivity
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.TinyDB
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

internal class CreateElectionViewModel(
  application: Application,
  private val context: Context
) : AndroidViewModel(application) {
  private val electionDetailsSharedPrefs =
    ElectionDetailsSharedPrefs(getApplication())
  private val sharedPrefs = SharedPrefs(getApplication())
  private val tinydb = TinyDB(getApplication())
  private var loadToast: LoadToast? = null
  public var createElectionListener: CreateElectionListener? = null

  @RequiresApi(api = VERSION_CODES.KITKAT) fun createElection() {
    loadToast = LoadToast(context)
    loadToast!!.setText("Creating Election")
    loadToast!!.show()
    val candidates = tinydb.getListString("Candidates")
    val jsArray = JSONArray(candidates)
    val token = sharedPrefs.token
    val jsonObject = JSONObject()
    try {
      val map: MutableMap<String, String> =
        HashMap()
      map["voteBallot"] = ""
      map["hash"] = ""
      val ballot =
        JSONArray(arrayOf<Map<*, *>>(map))
      jsonObject.put("ballot", ballot) //Append the other JSONObject to the parent one
      jsonObject.put("name", electionDetailsSharedPrefs.electionName)
      jsonObject.put("description", electionDetailsSharedPrefs.electionDesc)
      jsonObject.put("voterListVisibility", electionDetailsSharedPrefs.voterListVisibility)
      jsonObject.put("startingDate", electionDetailsSharedPrefs.startTime)
      jsonObject.put("endingDate", electionDetailsSharedPrefs.endTime)
      jsonObject.put("isInvite", electionDetailsSharedPrefs.isInvite)
      jsonObject.put("ballotVisibility", electionDetailsSharedPrefs.ballotVisibility)
      jsonObject.put("isRealTime", electionDetailsSharedPrefs.isRealTime)
      jsonObject.put("votingAlgo", electionDetailsSharedPrefs.votingAlgo)
      jsonObject.put("candidates", jsArray)
      jsonObject.put("noVacancies", 1)
      jsonObject.put("electionType", "Election")
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService = RetrofitClient.getAPIService()
    val electionResponse =
      apiService.createElection(jsonObject.toString(), token)
    electionResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          loadToast!!.success()
          Toast.makeText(
              getApplication(), "Created Successfully", Toast.LENGTH_SHORT
          )
              .show()
          electionDetailsSharedPrefs.clearElectionData()
          createElectionListener?.onElectionUploadSuccess()
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
        call: Call<String?>,
        t: Throwable
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