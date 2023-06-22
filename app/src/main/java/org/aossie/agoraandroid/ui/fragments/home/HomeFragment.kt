package org.aossie.agoraandroid.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.fragments.auth.login.LoginViewModel
import org.aossie.agoraandroid.ui.screens.home.HomeScreen
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.ActiveElectionClick
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.CreateElectionClick
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.FinishedElectionClick
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.PendingElectionClick
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.Refresh
import org.aossie.agoraandroid.ui.screens.home.events.HomeScreenEvents.TotalElectionClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import org.aossie.agoraandroid.utilities.ResponseUI
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
) : BaseFragment(viewModelFactory) {

  private val homeViewModel: HomeViewModel by viewModels {
    viewModelFactory
  }

  private val loginViewModel: LoginViewModel by viewModels {
    viewModelFactory
  }

  private lateinit var composeView: ComposeView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  override fun onFragmentInitiated() {
    loginViewModel.sessionExpiredListener = this
    composeView.setContent {
      val homeScreenDataState by homeViewModel.countMediatorLiveData.observeAsState()
      val progressErrorState by homeViewModel.progressAndErrorState.collectAsState()
      AgoraTheme {
        HomeScreen(homeScreenDataState,progressErrorState){ event ->
          when(event){
            ActiveElectionClick -> {
              findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToActiveElectionsFragment())
            }
            CreateElectionClick -> {
              findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToCreateElectionFragment())
            }
            FinishedElectionClick -> {
              findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToFinishedElectionsFragment())
            }
            PendingElectionClick -> {
              findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToPendingElectionsFragment())
            }
            TotalElectionClick -> {
              findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToElectionsFragment())
            }
            Refresh -> {
              updateUi()
            }
          }
        }
      }
    }

    lifecycleScope.launch {
      loginViewModel.getLoginStateFlow.collect {
        if (it != null) {
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
            else -> {}
          }
        }
      }
    }

    lifecycleScope.launch {
      loginViewModel.getLoggedInUser()
        .collect { user ->
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

    updateUi()
  }

  override fun onNetworkConnected() {
    updateUi()
  }

  override fun onDestroyView() {
    homeViewModel.sessionExpiredListener = null
    loginViewModel.sessionExpiredListener = null
    super.onDestroyView()
  }

  private fun updateUi() {
    lifecycleScope.launch {
      preferenceProvider.setUpdateNeeded(true)
      homeViewModel.getElections()
    }
  }
}
