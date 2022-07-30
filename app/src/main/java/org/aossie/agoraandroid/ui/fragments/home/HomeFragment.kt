package org.aossie.agoraandroid.ui.fragments.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.takusemba.spotlight.Spotlight
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R.color
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.common.utilities.ResponseUI
import org.aossie.agoraandroid.common.utilities.TargetData
import org.aossie.agoraandroid.common.utilities.getSpotlight
import org.aossie.agoraandroid.common.utilities.scrollToView
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.databinding.FragmentHomeBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

class HomeFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val preferenceProvider: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private lateinit var binding: FragmentHomeBinding

  private var spotlight: Spotlight? = null
  private var spotlightTargets: ArrayList<TargetData>? = null
  private var currentSpotlightIndex = 0

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentHomeBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {
    loginViewModel.sessionExpiredListener = this
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
    binding.swipeRefresh.setOnRefreshListener { updateUi() }

    loginViewModel.getLoginLiveData.observe(
      viewLifecycleOwner,
      {
        when (it.status) {
          ResponseUI.Status.LOADING -> {
            // Do Nothing
          }
          ResponseUI.Status.SUCCESS -> {
            updateUi()
          }
          ResponseUI.Status.ERROR -> {
            notify(it.message)
          }
        }
      }
    )

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

    homeViewModel.countMediatorLiveData.observe(
      viewLifecycleOwner,
      {
        binding.textViewActiveCount.text = it[ACTIVE_ELECTION_COUNT].toString()
        binding.textViewTotalCount.text = it[TOTAL_ELECTION_COUNT].toString()
        binding.textViewPendingCount.text = it[PENDING_ELECTION_COUNT].toString()
        binding.textViewFinishedCount.text = it[FINISHED_ELECTION_COUNT].toString()
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE
        binding.constraintLayout.visibility = View.VISIBLE
        binding.swipeRefresh.isRefreshing = false // Disables the refresh icon
      }
    )
    updateUi()
    binding.root.doOnLayout {
      checkIsFirstOpen()
    }
  }

  override fun onNetworkConnected() {
    updateUi()
  }

  override fun onDestroyView() {
    binding.swipeRefresh.setOnRefreshListener(null)
    homeViewModel.sessionExpiredListener = null
    loginViewModel.sessionExpiredListener = null
    super.onDestroyView()
  }

  private fun updateUi() {
    lifecycleScope.launch {
      preferenceProvider.setUpdateNeeded(true)
      homeViewModel.getElections()
    }
    binding.shimmerViewContainer.startShimmer()
    binding.shimmerViewContainer.visibility = View.VISIBLE
    binding.constraintLayout.visibility = View.GONE
  }

  private fun checkIsFirstOpen() {
    lifecycleScope.launch {
      if (!preferenceProvider.isDisplayed(binding.root.id.toString())
        .first()
      ) {
        spotlightTargets = getSpotlightTargets()
        preferenceProvider.setDisplayed(binding.root.id.toString())
        showSpotlight()
      }
    }
  }

  private fun showSpotlight() {
    spotlightTargets?.let {
      if (currentSpotlightIndex in it.indices) {
        scrollToView(binding.scrollView, it[currentSpotlightIndex].targetView)
        spotlight = requireActivity().getSpotlight(
          it[currentSpotlightIndex++],
          {
            destroySpotlight()
          },
          {
            it.clear()
            destroySpotlight()
          },
          {
            if (isAdded) {
              showSpotlight()
            }
          }
        )
        spotlight?.start()
      }
    }
  }

  private fun getSpotlightTargets(): ArrayList<TargetData> {
    val targetData = ArrayList<TargetData>()
    targetData.add(
      TargetData(
        binding.buttonCreateElection, getString(string.Create_Election),
        getString(string.create_election_spotlight)
      )
    )
    return targetData
  }

  private fun destroySpotlight() {
    spotlight?.finish()
    spotlight = null
  }

  override fun onPause() {
    super.onPause()
    destroySpotlight()
  }

  override fun onConfigurationChanged(newConfig: Configuration) {
    super.onConfigurationChanged(newConfig)
    destroySpotlight()
  }
}
