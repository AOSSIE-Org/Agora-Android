package org.aossie.agoraandroid.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import org.aossie.agoraandroid.AgoraApp
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.InternetManager
import org.aossie.agoraandroid.utilities.NetworkStatus
import org.aossie.agoraandroid.utilities.snackbar

abstract class BaseFragment <T : ViewBinding>(
  private val viewModelFactory: ViewModelProvider.Factory,
) : Fragment(), SessionExpiredListener {

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private lateinit var internetManager: InternetManager

  private lateinit var _binding: ViewBinding
  abstract val bindingInflater: (LayoutInflater) -> T
  protected val isConnected
    get() = internetManager.isConnected()

  @Suppress("UNCHECKED_CAST")
  protected val binding: T
    get() = _binding as T

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = bindingInflater.invoke(layoutInflater)
    onFragmentInitiated()
    internetManager = (activity?.application as AgoraApp).appComponent.getInternetManager()

    return _binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Coroutines.main {
      hostViewModel.onNetworkChanged(internetManager.isConnected())
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
  }

  abstract fun onFragmentInitiated()
  open fun onNetworkConnected() {}
  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
  fun notify(msg: String?) {
    if (msg == context?.getText(R.string.no_network))
      hostViewModel.onNetworkChanged(false)
    else
      binding.root.snackbar(msg)
  }
}
