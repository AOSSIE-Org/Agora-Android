package org.aossie.agoraandroid.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
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
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginFragmentDirections
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import org.aossie.agoraandroid.utilities.toggleIsEnable
import timber.log.Timber
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
) : Fragment(), SessionExpiredListener {

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var rootView: View
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    rootView = inflater.inflate(layout.fragment_home, container, false)

    loginViewModel.sessionExpiredListener = this
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
    rootView.button_create_election.setOnClickListener {
      Navigation.findNavController(rootView)
        .navigate(HomeFragmentDirections.actionHomeFragmentToCreateElectionFragment())
    }
    rootView.swipe_refresh.setOnRefreshListener { updateUi() }

    loginViewModel.getLoginLiveData.observe(viewLifecycleOwner,{
      when(it.status){
        ResponseUI.Status.LOADING ->  {
        //Do Nothing
        }
        ResponseUI.Status.SUCCESS ->{
         updateUi()
        }
        ResponseUI.Status.ERROR ->{
          rootView.snackbar(it.message?:"")
        }
      }
    })

    loginViewModel.getLoggedInUser()
      .observe(
        viewLifecycleOwner,
        Observer { user ->
          if (user != null) {
            val formatter =
              SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            // Timber.d(user.token!!)
            val currentDate = Calendar.getInstance()
              .time
            val expireOn = user.authTokenExpiresOn
            Timber.d(user.toString())
            Timber.tag("expiresOn")
              .d(expireOn.toString())
            try {
              if (expireOn != null) {
                val expiresOn = formatter.parse(expireOn)
                // If the token is expired, get a new one to continue login session of user
                if (currentDate.after(expiresOn)) {
                  Timber.tag("expired")
                    .d(expireOn.toString())
                  if (preferenceProvider.getIsFacebookUser()) {
                    loginViewModel.facebookLogInRequest()
                  } else {
                    loginViewModel.refreshAccessToken(user.trustedDevice)
                  }
                }
              }
            } catch (e: ParseException) {
              e.printStackTrace()
            }
          }
        }
      )
    Coroutines.main {
      val totalElectionCount = homeViewModel.totalElectionsCount.await()
      val pendingElectionCount = homeViewModel.pendingElectionsCount.await()
      val activeElectionCount = homeViewModel.activeElectionsCount.await()
      val finishedElectionCount = homeViewModel.finishedElectionsCount.await()
      if (view != null) {
        homeViewModel.getElections()
          .observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
              totalElectionCount.observe(
                viewLifecycleOwner,
                Observer {
                  rootView.text_view_total_count.text = it.toString()
                  pendingElectionCount.observe(
                    viewLifecycleOwner,
                    Observer { pending ->
                      rootView.text_view_pending_count.text = pending.toString()
                      finishedElectionCount.observe(
                        viewLifecycleOwner,
                        Observer { finished ->
                          rootView.text_view_finished_count.text = finished.toString()
                          activeElectionCount.observe(
                            viewLifecycleOwner,
                            Observer { active ->
                              rootView.text_view_active_count.text = active.toString()
                              rootView.shimmer_view_container.stopShimmer()
                              rootView.shimmer_view_container.visibility = View.GONE
                              rootView.constraintLayout.visibility = View.VISIBLE
                              rootView.swipe_refresh.isRefreshing =
                                false // Disables the refresh icon
                            }
                          )
                        }
                      )
                    }
                  )
                }
              )
            }
          )
      }
    }
    return rootView
  }

  override fun onResume() {
    super.onResume()
    rootView.shimmer_view_container.startShimmer()
  }

  override fun onPause() {
    rootView.shimmer_view_container.stopShimmer()
    super.onPause()
  }

  override fun onDestroyView() {
    rootView.swipe_refresh.setOnRefreshListener(null)
    homeViewModel.sessionExpiredListener = null
    loginViewModel.sessionExpiredListener = null
    super.onDestroyView()
  }

  private fun updateUi() {
    preferenceProvider.setUpdateNeeded(true)
    Navigation.findNavController(rootView)
      .navigate(R.id.homeFragment)
  }


  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
