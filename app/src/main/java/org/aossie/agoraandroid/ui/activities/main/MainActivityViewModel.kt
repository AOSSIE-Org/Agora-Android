package org.aossie.agoraandroid.ui.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.useCases.homeFragment.DeleteUserUseCase
import javax.inject.Inject

class MainActivityViewModel
@Inject
constructor(
  private val deleteUserUseCase: DeleteUserUseCase
) : ViewModel() {

  private val mutableIsLogout = MutableStateFlow<Boolean?>(null)
  private val _getNetworkStatusStateFlow = MutableStateFlow<Boolean?>(null)
  val getNetworkStatusStateFlow = _getNetworkStatusStateFlow.asStateFlow()
  val isLogout: StateFlow<Boolean?> get() = mutableIsLogout

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
