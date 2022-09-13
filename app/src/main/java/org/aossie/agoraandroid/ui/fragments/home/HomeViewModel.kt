package org.aossie.agoraandroid.ui.fragments.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.useCases.homeFragment.HomeFragmentUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

const val TOTAL_ELECTION_COUNT = "totalElectionsCount"
const val PENDING_ELECTION_COUNT = "pendingElectionsCount"
const val FINISHED_ELECTION_COUNT = "finishedElectionsCount"
const val ACTIVE_ELECTION_COUNT = "activeElectionsCount"

class HomeViewModel @Inject
constructor(
  private val homeViewModelUseCases: HomeFragmentUseCases
) : ViewModel() {
  private val _getLogoutStateFLow: MutableStateFlow<ResponseUI<Any>?> = MutableStateFlow(null)
  val getLogoutStateFlow: StateFlow<ResponseUI<Any>?> = _getLogoutStateFLow
  var sessionExpiredListener: SessionExpiredListener? = null
  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
    .time
  private val date: String = formatter.format(currentDate)

  private val _countMediatorLiveData = MediatorLiveData<MutableMap<String, Int>>()
  val countMediatorLiveData = _countMediatorLiveData

  init {
    _countMediatorLiveData.value = mutableMapOf()
    addSource()
  }

  fun getElections() {
    GlobalScope.launch {
      homeViewModelUseCases.fetchAndSaveElection()
    }
  }

  private fun addSource() {
    viewModelScope.launch {
      _countMediatorLiveData.addSource(homeViewModelUseCases.getTotalElectionsCount()) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[TOTAL_ELECTION_COUNT] = value
          }
        }
      }
      _countMediatorLiveData.addSource(homeViewModelUseCases.getPendingElectionsCount(date)) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[PENDING_ELECTION_COUNT] = value
          }
        }
      }
      _countMediatorLiveData.addSource(homeViewModelUseCases.getActiveElectionsCount(date)) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[ACTIVE_ELECTION_COUNT] = value
          }
        }
      }

      _countMediatorLiveData.addSource(homeViewModelUseCases.getFinishedElectionsCount(date)) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[FINISHED_ELECTION_COUNT] = value
          }
        }
      }
    }
  }

  fun deleteUserData() {
    viewModelScope.launch {
      homeViewModelUseCases.deleteUser()
    }
  }

  fun doLogout() {
    _getLogoutStateFLow.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        homeViewModelUseCases.logOut()
        _getLogoutStateFLow.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      }
    }
  }
}
