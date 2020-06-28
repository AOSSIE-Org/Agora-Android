package org.aossie.agoraandroid.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_home.view.button_create_election
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
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.remote.RetrofitClient
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.fragments.createelection.ElectionDetailsSharedPrefs
import org.aossie.agoraandroid.ui.fragments.moreOptions.HomeViewModel
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.SharedPrefs
import org.aossie.agoraandroid.utilities.showActionBar
import org.aossie.agoraandroid.utilities.snackbar
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class HomeFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
    private val preferenceProvider: PreferenceProvider
) : Fragment(), AuthListener {
  private var electionDetailsSharedPrefs: ElectionDetailsSharedPrefs? = null
  private var sharedPrefs: SharedPrefs? = null

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels{
    viewModelFactory
  }

  private lateinit var rootView: View
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    electionDetailsSharedPrefs = ElectionDetailsSharedPrefs(context!!)
    sharedPrefs = SharedPrefs(activity!!)
    rootView = inflater.inflate(layout.fragment_home, container, false)
    showActionBar()
    loginViewModel.authListener = this
    rootView.swipe_refresh.setColorSchemeResources(color.logo_yellow, color.logo_green)
    loginViewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer {
      val formatter =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
      val currentDate = Calendar.getInstance()
          .time
        val expireOn = it.expiredAt
      Log.d("expiresOn", expireOn)
      try {
        if (expireOn != null) {
          val expiresOn = formatter.parse(expireOn)
          //If the token is expired, get a new one to continue login session of user
          if (currentDate.after(expiresOn)) {
            loginViewModel.logInRequest(it.username!!, it.password!!)
          }
        }
      }catch (e: ParseException){
        e.printStackTrace()
      }
    })
    getElectionData(sharedPrefs!!.token)

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
    rootView.button_create_election.setOnClickListener {
      Navigation.findNavController(rootView)
          .navigate(HomeFragmentDirections.actionHomeFragmentToUploadElectionDetailsFragment())
    }
    rootView.swipe_refresh.setOnRefreshListener(
        OnRefreshListener { doYourUpdate() }
    )

    Coroutines.main {
      val totalElectionCount = homeViewModel.totalElectionsCount.await()
      val pendingElectionCount = homeViewModel.pendingElectionsCount.await()
      val activeElectionCount = homeViewModel.activeElectionsCount.await()
      val finishedElectionCount = homeViewModel.finishedElectionsCount.await()
      val elections = homeViewModel.elections.await()
      if(view != null) {
        totalElectionCount.observe(viewLifecycleOwner, Observer {
          rootView.text_view_total_count.text = it.toString()
        })
        pendingElectionCount.observe(viewLifecycleOwner, Observer {
          rootView.text_view_pending_count.text = it.toString()
        })
        finishedElectionCount.observe(viewLifecycleOwner, Observer {
          rootView.text_view_finished_count.text = it.toString()
        })
        activeElectionCount.observe(viewLifecycleOwner, Observer {
          rootView.text_view_active_count.text = it.toString()
        })
        elections.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
          rootView.shimmer_view_container.stopShimmer()
          rootView.shimmer_view_container.visibility = View.GONE
          rootView.constraintLayout.visibility = View.VISIBLE
          rootView.swipe_refresh.isRefreshing = false // Disables the refresh icon
        })
      }

    }

    return rootView
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
          electionDetailsSharedPrefs!!.saveElectionDetails(response.body())
        }
      }

      override fun onFailure(
        call: Call<String?>,
        t: Throwable
      ) {
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
    preferenceProvider.setUpdateNeeded(true)
    Navigation.findNavController(rootView).navigate(R.id.homeFragment)
    getElectionData(sharedPrefs!!.token) //try to fetch data again
  }

  override fun onSuccess() {
    doYourUpdate()
  }

  override fun onStarted() {
    // do nothing
  }

  override fun onFailure(message: String) {
    rootView.snackbar("$message (Token Expired) ")
  }
}