package org.aossie.agoraandroid.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.android.synthetic.main.fragment_home.view.swipe_refresh
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.createelection.CreateElectionOne
import org.aossie.agoraandroid.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.displayelections.ActiveElectionsActivity
import org.aossie.agoraandroid.displayelections.FinishedElectionsActivity
import org.aossie.agoraandroid.displayelections.PendingElectionsActivity
import org.aossie.agoraandroid.displayelections.TotalElectionsActivity
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
  private var mActiveCountTextView: TextView? = null
  private var mPendingCountTextView: TextView? = null
  private var mTotalCountTextView: TextView? = null
  private var mFinishedCountTextView: TextView? = null
  private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
  private var mActiveCount = 0
  private var mFinishedCount = 0
  private var mPendingCount = 0
  private var flag = 0
  private var mShimmerViewContainer: ShimmerFrameLayout? = null
  private var constraintLayout: ConstraintLayout? = null
  private var sharedPrefs: SharedPrefs? = null
  private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(activity)
    sharedPrefs = SharedPrefs(activity!!)
    val view = inflater.inflate(layout.fragment_home, container, false)
    showActionBar()
    mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container)
    constraintLayout = view.findViewById(R.id.constraintLayout)
    mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh)
    view.swipe_refresh.setColorSchemeResources(color.logo_yellow, color.logo_green)
    val mTotalElectionsCardView: CardView = view.findViewById(R.id.card_view_total_elections)
    val mPendingElectionsCardView: CardView = view.findViewById(R.id.card_view_pending_elections)
    val mActiveElectionsCardView: CardView = view.findViewById(R.id.card_view_active_elections)
    val mFinishedElectionsCardView: CardView = view.findViewById(R.id.card_view_finished_elections)
    mActiveCountTextView = view.findViewById(R.id.text_view_active_count)
    mPendingCountTextView = view.findViewById(R.id.text_view_pending_count)
    mTotalCountTextView = view.findViewById(R.id.text_view_total_count)
    mFinishedCountTextView = view.findViewById(R.id.text_view_finished_count)
    val createElection =
      view.findViewById<Button>(R.id.button_create_election)
    mActiveElectionsCardView.setOnClickListener {
      startActivity(
          Intent(
              activity, ActiveElectionsActivity::class.java
          )
      )
    }
    mPendingElectionsCardView.setOnClickListener {
      startActivity(
          Intent(activity, PendingElectionsActivity::class.java)
      )
    }
    mFinishedElectionsCardView.setOnClickListener {
      startActivity(
          Intent(activity, FinishedElectionsActivity::class.java)
      )
    }
    mTotalElectionsCardView.setOnClickListener {
      startActivity(
          Intent(activity, TotalElectionsActivity::class.java)
      )
    }
    createElection.setOnClickListener {
      startActivity(
          Intent(activity, CreateElectionOne::class.java)
      )
    }
    view.swipe_refresh.setOnRefreshListener(
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
    return view
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    super.onViewCreated(view, savedInstanceState)
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
          mShimmerViewContainer!!.stopShimmer()
          mShimmerViewContainer!!.visibility = View.GONE
          constraintLayout!!.visibility = View.VISIBLE
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
          mShimmerViewContainer!!.stopShimmer()
          mShimmerViewContainer!!.visibility = View.GONE
          constraintLayout!!.visibility = View.VISIBLE
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
            mTotalCountTextView!!.text = jsonArray.length()
                .toString()
            mPendingCountTextView!!.text = mPendingCount.toString()
            mActiveCountTextView!!.text = mActiveCount.toString()
            mFinishedCountTextView!!.text = mFinishedCount.toString()
            mSwipeRefreshLayout!!.isRefreshing = false // Disables the refresh icon
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
        mShimmerViewContainer!!.stopShimmer()
        mShimmerViewContainer!!.visibility = View.GONE
        constraintLayout!!.visibility = View.VISIBLE
        mSwipeRefreshLayout!!.isRefreshing = false // Disables the refresh icon
        Toast.makeText(activity, "Something went wrong please refresh", Toast.LENGTH_SHORT)
            .show()
      }
    })
  }

  override fun onResume() {
    super.onResume()
    mShimmerViewContainer!!.startShimmer()
  }

  override fun onPause() {
    mShimmerViewContainer!!.stopShimmer()
    super.onPause()
  }

  private fun doYourUpdate() {
    constraintLayout!!.visibility = View.GONE
    mShimmerViewContainer!!.visibility = View.VISIBLE
    mShimmerViewContainer!!.startShimmer()
    getElectionData(sharedPrefs!!.token) //try to fetch data again
  }
}