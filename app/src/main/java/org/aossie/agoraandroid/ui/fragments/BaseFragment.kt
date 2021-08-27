package org.aossie.agoraandroid.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.InternetManager
import org.aossie.agoraandroid.utilities.NetworkStatus
import org.aossie.agoraandroid.utilities.snackbar

abstract class BaseFragment(
  private val viewModelFactory: ViewModelProvider.Factory,
) : Fragment(), SessionExpiredListener {

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var internetManager: InternetManager
  protected val isConnected
    get() = internetManager.isConnected()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    internetManager = (activity?.application as AgoraApp).appComponent.getInternetManager()
    lifecycleScope.launch {
      hostViewModel.onNetworkChanged(isConnected)
      internetManager.networkStatus.collect {
        when (it) {
          NetworkStatus.Available -> {
            withContext(Dispatchers.Main) {
              onNetworkConnected()
              hostViewModel.onNetworkChanged(true)
            }
          }
          else -> {
            withContext(Dispatchers.Main) {
              hostViewModel.onNetworkChanged(false)
            }
          }
        }
      }
    }
    onFragmentInitiated()
  }

  open fun onNetworkConnected() {}
  abstract fun onFragmentInitiated()
  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
  fun notify(msg: String?) {
    if (msg == context?.getText(R.string.no_network))
      hostViewModel.onNetworkChanged(false)
    else
      requireView().snackbar(msg)
  }
}
