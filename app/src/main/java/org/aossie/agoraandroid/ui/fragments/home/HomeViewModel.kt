package org.aossie.agoraandroid.ui.fragments.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.use_cases.home_fragment.HomeFragmentUseCases
import org.aossie.agoraandroid.data.Repository.ElectionsRepositoryImpl
import org.aossie.agoraandroid.data.Repository.UserRepositoryImpl
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
  private val _getLogoutLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getLogoutLiveData = _getLogoutLiveData
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
      homeViewModelUseCases.fetchAndSaveElectionUseCase()
    }
  }

  private fun addSource() {
    viewModelScope.launch {
      _countMediatorLiveData.addSource(homeViewModelUseCases.getTotalElectionsCountUseCase()) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[TOTAL_ELECTION_COUNT] = value
          }
        }
      }
      _countMediatorLiveData.addSource(homeViewModelUseCases.getPendingElectionsCountUseCase(date)) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[PENDING_ELECTION_COUNT] = value
          }
        }
      }
      _countMediatorLiveData.addSource(homeViewModelUseCases.getActiveElectionsCountUseCase(date)) { value ->
        _countMediatorLiveData.value = _countMediatorLiveData.value.apply {
          this?.let {
            this[ACTIVE_ELECTION_COUNT] = value
          }
        }
      }

      _countMediatorLiveData.addSource(homeViewModelUseCases.getFinishedElectionsCountUseCase(date)) { value ->
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
      homeViewModelUseCases.deleteUserUseCase()
    }
  }

  fun doLogout() {
    _getLogoutLiveData.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        homeViewModelUseCases.logOutUseCase()
        _getLogoutLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getLogoutLiveData.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLogoutLiveData.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        _getLogoutLiveData.value = ResponseUI.error(e.message)
      }
    }
  }
}
