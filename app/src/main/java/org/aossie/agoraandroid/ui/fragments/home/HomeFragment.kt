package org.aossie.agoraandroid.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentHomeBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.snackbar
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
) : Fragment(), AuthListener {

  private lateinit var binding: FragmentHomeBinding

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

    loginViewModel.authListener = this
    binding.swipeRefresh.setColorSchemeResources(color.logo_yellow, color.logo_green)

    binding.cardViewActiveElections.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToActiveElectionsFragment())
    }
    binding.cardViewPendingElections.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToPendingElectionsFragment())
    }
    binding.cardViewFinishedElections.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToFinishedElectionsFragment())
    }
    binding.cardViewTotalElections.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToElectionsFragment())
    }
    binding.buttonCreateElection.setOnClickListener {
      Navigation.findNavController(binding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToCreateElectionFragment())
    }
    binding.swipeRefresh.setOnRefreshListener { doYourUpdate() }

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
                  lifecycleScope.launch {
                    if (preferenceProvider.getIsFacebookUser()
                      .first()
                    ) {
                      loginViewModel.facebookLogInRequest()
                    } else {
                      loginViewModel.refreshAccessToken(user.trustedDevice)
                    }
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
                  binding.textViewTotalCount.text = it.toString()
                  pendingElectionCount.observe(
                    viewLifecycleOwner,
                    Observer { pending ->
                      binding.textViewPendingCount.text = pending.toString()
                      finishedElectionCount.observe(
                        viewLifecycleOwner,
                        Observer { finished ->
                          binding.textViewFinishedCount.text = finished.toString()
                          activeElectionCount.observe(
                            viewLifecycleOwner,
                            Observer { active ->
                              binding.textViewActiveCount.text = active.toString()
                              binding.shimmerViewContainer.stopShimmer()
                              binding.shimmerViewContainer.visibility = View.GONE
                              binding.constraintLayout.visibility = View.VISIBLE
                              binding.swipeRefresh.isRefreshing =
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
    return binding.root
  }

  override fun onResume() {
    super.onResume()
    binding.shimmerViewContainer.startShimmer()
  }

  override fun onPause() {
    binding.shimmerViewContainer.stopShimmer()
    super.onPause()
  }

  override fun onDestroyView() {
    binding.swipeRefresh.setOnRefreshListener(null)
    homeViewModel.authListener = null
    loginViewModel.authListener = null
    super.onDestroyView()
  }

  private fun doYourUpdate() {
    lifecycleScope.launch {
      preferenceProvider.setUpdateNeeded(true)
    }
    Navigation.findNavController(binding.root)
      .navigate(R.id.homeFragment)
  }

  override fun onSuccess(message: String?) {
    doYourUpdate()
  }

  override fun onStarted() {
    // do nothing
  }

  override fun onFailure(message: String) {
    binding.root.snackbar(message)
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
