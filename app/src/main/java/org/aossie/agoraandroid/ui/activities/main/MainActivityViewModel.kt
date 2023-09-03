package org.aossie.agoraandroid.ui.activities.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.useCases.homeFragment.DeleteUserUseCase
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityComposeState
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.AppBarVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.BackBtnVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.BottomNavVisibility
import org.aossie.agoraandroid.ui.activities.main.component.MainActivityEvent.ShowTitle
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
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

  private val _progressAndErrorState = mutableStateOf(ScreensState())
  val progressAndErrorState: State<ScreensState> = _progressAndErrorState

  private val _mainActivityState = mutableStateOf(MainActivityComposeState())
  val mainActivityState:State<MainActivityComposeState> = _mainActivityState


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

  fun onEvent(event: MainActivityEvent) {
    when(event) {
      is AppBarVisibility -> {
        _mainActivityState.value = mainActivityState.value.copy(
          isAppBarVisible = event.isVisible
        )
      }
      is BackBtnVisibility -> {
        _mainActivityState.value = mainActivityState.value.copy(
          isBackBtnVisible = event.isVisible
        )
      }
      is BottomNavVisibility -> {
        _mainActivityState.value = mainActivityState.value.copy(
          isBottomNavVisible = event.isVisible
        )
      }
      is ShowTitle -> {
        _mainActivityState.value = mainActivityState.value.copy(
          title = event.title
        )
      }
    }
  }
}
