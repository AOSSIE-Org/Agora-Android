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
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentHomeBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
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

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var mBinding: FragmentHomeBinding
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    mBinding = FragmentHomeBinding.inflate(inflater)
    loginViewModel.authListener = this
    homeViewModel.getElections()
    mBinding.swipeRefresh.setColorSchemeResources(color.logo_yellow, color.logo_green)

    mBinding.cardViewActiveElections.setOnClickListener {
      Navigation.findNavController(mBinding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToActiveElectionsFragment())
    }
    mBinding.cardViewPendingElections.setOnClickListener {
      Navigation.findNavController(mBinding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToPendingElectionsFragment())
    }
    mBinding.cardViewFinishedElections.setOnClickListener {
      Navigation.findNavController(mBinding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToFinishedElectionsFragment())
    }
    mBinding.cardViewTotalElections.setOnClickListener {
      Navigation.findNavController(mBinding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToElectionsFragment())
    }
    mBinding.buttonCreateElection.setOnClickListener {
      Navigation.findNavController(mBinding.root)
        .navigate(HomeFragmentDirections.actionHomeFragmentToCreateElectionFragment())
    }
    mBinding.swipeRefresh.setOnRefreshListener { doYourUpdate() }

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

    homeViewModel.countMediatorLiveData.observe(
      viewLifecycleOwner,
      {
        mBinding.textViewActiveCount.text = it[ACTIVE_ELECTION_COUNT].toString()
        mBinding.textViewTotalCount.text = it[TOTAL_ELECTION_COUNT].toString()
        mBinding.textViewPendingCount.text = it[PENDING_ELECTION_COUNT].toString()
        mBinding.textViewFinishedCount.text = it[FINISHED_ELECTION_COUNT].toString()
        mBinding.shimmerViewContainer.stopShimmer()
        mBinding.shimmerViewContainer.visibility = View.GONE
        mBinding.constraintLayout.visibility = View.VISIBLE
        mBinding.swipeRefresh.isRefreshing =
          false // Disables the refresh icon
      }
    )

    return mBinding.root
  }

  override fun onResume() {
    super.onResume()
    mBinding.shimmerViewContainer.startShimmer()
  }

  override fun onPause() {
    mBinding.shimmerViewContainer.stopShimmer()
    super.onPause()
  }

  override fun onDestroyView() {
    mBinding.swipeRefresh.setOnRefreshListener(null)
    homeViewModel.authListener = null
    loginViewModel.authListener = null
    super.onDestroyView()
  }

  private fun doYourUpdate() {
    preferenceProvider.setUpdateNeeded(true)
    homeViewModel.getElections()
  }

  override fun onSuccess(message: String?) {
    doYourUpdate()
  }

  override fun onStarted() {
    // do nothing
  }

  override fun onFailure(message: String) {
    mBinding.root.snackbar(message)
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
