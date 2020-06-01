package org.aossie.agoraandroid.ui.fragments.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.android.synthetic.main.data_placeholder_shimmer_layout.view.btn_create_election
import kotlinx.android.synthetic.main.fragment_home.view.card_view_active_elections
import kotlinx.android.synthetic.main.fragment_home.view.card_view_finished_elections
import kotlinx.android.synthetic.main.fragment_home.view.card_view_pending_elections
import kotlinx.android.synthetic.main.fragment_home.view.card_view_total_elections
import kotlinx.android.synthetic.main.fragment_home.view.constraintLayout
import kotlinx.android.synthetic.main.fragment_home.view.shimmer_view_container
import kotlinx.android.synthetic.main.fragment_home.view.swipe_refresh
import kotlinx.android.synthetic.main.fragment_home.view.text_view_active_count
import kotlinx.android.synthetic.main.fragment_home.view.text_view_finished_count
import kotlinx.android.synthetic.main.fragment_home.view.text_view_pending_count
import kotlinx.android.synthetic.main.fragment_home.view.text_view_total_count
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.createelection.CreateElectionOne
import org.aossie.agoraandroid.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.showActionBar
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {
  private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
  private var mActiveCount = 0
  private var mFinishedCount = 0
  private var mPendingCount = 0
  private var flag = 0
  private var sharedPrefs: SharedPrefs? = null

  private lateinit var rootView: View
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(activity)
    sharedPrefs = SharedPrefs(activity!!)
    rootView = inflater.inflate(layout.fragment_home, container, false)
    showActionBar()
    rootView.swipe_refresh.setColorSchemeResources(color.logo_yellow, color.logo_green)
    rootView.card_view_active_elections.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(HomeFragmentDirections.actionHomeFragmentToActiveElectionsFragment())
    }
    rootView.card_view_pending_elections.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(HomeFragmentDirections.actionHomeFragmentToPendingElectionsFragment())
    }
    rootView.card_view_finished_elections.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(HomeFragmentDirections.actionHomeFragmentToFinishedElectionsFragment())
    }
    rootView.card_view_total_elections.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(HomeFragmentDirections.actionHomeFragmentToElectionsFragment())
    }
    rootView.btn_create_election.setOnClickListener {
      startActivity(
          Intent(context, CreateElectionOne::class.java)
      )
    }
    rootView.swipe_refresh.setOnRefreshListener(
        OnRefreshListener { doYourUpdate() }
    )
    val userName = sharedPrefs!!.userName
    val userPassword = sharedPrefs!!.pass
    val formatter =
      SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault())
    val currentDate = Calendar.getInstance()
        .time
    try {
      val expireOn = sharedPrefs!!.tokenExpiresOn
      if (expireOn != null) {
        val expiresOn = formatter.parse(expireOn)
        //If the token is expired, get a new one to continue login session of user
        if (currentDate.after(expiresOn)) {
          updateToken(userName, userPassword)
        }
      }
    } catch (e: ParseException) {
      e.printStackTrace()
    }
    getElectionData(sharedPrefs!!.token)
    return rootView
  }

  private fun updateToken(
    userName: String?,
    userPassword: String?
  ) {
    val jsonObject = JSONObject()
    try {
      jsonObject.put("identifier", userName)
      jsonObject.put("password", userPassword)
    } catch (e: JSONException) {
      e.printStackTrace()
    }
    val apiService = RetrofitClient.getAPIService()
    val logInResponse = apiService.logIn(jsonObject.toString())
    logInResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          rootView.shimmer_view_container.stopShimmer()
          rootView.shimmer_view_container.visibility = View.GONE
          rootView.constraintLayout.visibility = View.VISIBLE
          try {
            val jsonObjects = JSONObject(response.body())
            val token = jsonObjects.getJSONObject("token")
            val expiresOn = token.getString("expiresOn")
            val key = token.getString("token")
            sharedPrefs!!.saveToken(key)
            sharedPrefs!!.saveTokenExpiresOn(expiresOn)
          } catch (e: JSONException) {
            e.printStackTrace()
          }
        } else {
          Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT)
              .show()
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        Toast.makeText(context, "Something went wrong please try again", Toast.LENGTH_SHORT)
            .show()
      }
    })
  }

  private fun getElectionData(token: String?) {
    val apiService = RetrofitClient.getAPIService()
    val electionDataResponse = apiService.getAllElections(token)
    electionDataResponse.enqueue(object : Callback<String?> {
      override fun onResponse(
        call: Call<String?>,
        response: Response<String?>
      ) {
        if (response.message() == "OK") {
          rootView.shimmer_view_container.stopShimmer()
          rootView.shimmer_view_container.visibility = View.GONE
          rootView.constraintLayout.visibility = View.VISIBLE
          electionDetailsSharedPrefs!!.saveElectionDetails(response.body())
          try {
            val jsonObject = JSONObject(response.body())
            val jsonArray = jsonObject.getJSONArray("elections")
            for (i in 0 until jsonArray.length()) {
              val jsonObject1 = jsonArray.getJSONObject(i)
              val startingDate = jsonObject1.getString("start")
              val endingDate = jsonObject1.getString("end")
              val formatter =
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
              val formattedStartingDate = formatter.parse(startingDate)
              val formattedEndingDate = formatter.parse(endingDate)
              val currentDate = Calendar.getInstance()
                  .time

              // Separating into Active, Finished or Pending Elections
              if (flag == 0) {
                if (currentDate.before(formattedStartingDate)) {
                  mPendingCount++
                } else if (currentDate.after(formattedStartingDate) && currentDate.before(
                        formattedEndingDate
                    )
                ) {
                  mActiveCount++
                } else if (currentDate.after(formattedEndingDate)) {
                  mFinishedCount++
                }
              }
            }
            flag++
            rootView.text_view_total_count.text = jsonArray.length()
                .toString()
           rootView.text_view_pending_count.text = mPendingCount.toString()
            rootView.text_view_active_count.text = mActiveCount.toString()
            rootView.text_view_finished_count.text = mFinishedCount.toString()
            rootView.swipe_refresh.isRefreshing = false // Disables the refresh icon
          } catch (e: JSONException) {
            e.printStackTrace()
          } catch (e: ParseException) {
            e.printStackTrace()
          }
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
        rootView.shimmer_view_container.stopShimmer()
        rootView.shimmer_view_container.visibility = View.GONE
        rootView.constraintLayout.visibility = View.VISIBLE
        rootView.swipe_refresh.isRefreshing = false // Disables the refresh icon
        Toast.makeText(activity, "Something went wrong please refresh", Toast.LENGTH_SHORT)
            .show()
      }
    })
  }

  override fun onResume() {
    super.onResume()
    rootView.shimmer_view_container.startShimmer()
  }

  override fun onPause() {
    rootView.shimmer_view_container.stopShimmer()
    super.onPause()
  }

  private fun doYourUpdate() {
    rootView.constraintLayout.visibility = View.GONE
    rootView.shimmer_view_container.visibility = View.VISIBLE
    rootView.shimmer_view_container.startShimmer()
    getElectionData(sharedPrefs!!.token) //try to fetch data again
  }
}