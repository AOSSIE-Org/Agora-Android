package org.aossie.agoraandroid.ui.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.useCases.homeFragment.DeleteUserUseCase
import javax.inject.Inject

class MainActivityViewModel
@Inject
constructor(
  private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

  private val mutableIsLogout = MutableStateFlow<Boolean?>(null)
  val isLogout: StateFlow<Boolean?> = mutableIsLogout
  private val _getNetworkStatusStateFlow = MutableStateFlow<Boolean?>(null)
  val getNetworkStatusStateFlow: StateFlow<Boolean?> = _getNetworkStatusStateFlow

  fun setLogout(isLogout: Boolean) {
    mutableIsLogout.value = isLogout
  }

  fun onNetworkChanged(isConnected: Boolean) {
    _getNetworkStatusStateFlow.value = isConnected
  }

  fun deleteUserData() {
    viewModelScope.launch {
      deleteUserUseCase()
    }
  }
}
