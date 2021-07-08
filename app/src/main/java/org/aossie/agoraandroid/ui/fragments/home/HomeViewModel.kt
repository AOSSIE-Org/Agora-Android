package org.aossie.agoraandroid.ui.fragments.home

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
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
  private val electionsRepository: ElectionsRepository,
  private val userRepository: UserRepository
) : ViewModel() {
  var authListener: AuthListener ? = null
  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
    .time
  private val date: String = formatter.format(currentDate)
  private val _countMediatorLiveData = MediatorLiveData<MutableMap<String, Int>>()
  val countMediatorLiveData = _countMediatorLiveData
  init {
    countMediatorLiveData.value = mutableMapOf()
    addSource()
  }

  fun getElections() {
    GlobalScope.launch {
      electionsRepository.fetchAndSaveElections()
    }
  }

  private fun addSource() {
    viewModelScope.launch {
      countMediatorLiveData.addSource(electionsRepository.getTotalElectionsCount()) { value ->
        countMediatorLiveData.value = countMediatorLiveData.value.apply {
          this?.let {
            this[TOTAL_ELECTION_COUNT] = value
          }
        }
      }
      countMediatorLiveData.addSource(electionsRepository.getPendingElectionsCount(date)) { value ->
        countMediatorLiveData.value = countMediatorLiveData.value.apply {
          this?.let {
            this[PENDING_ELECTION_COUNT] = value
          }
        }
      }
      countMediatorLiveData.addSource(electionsRepository.getActiveElectionsCount(date)) { value ->
        countMediatorLiveData.value = countMediatorLiveData.value.apply {
          this?.let {
            this[ACTIVE_ELECTION_COUNT] = value
          }
        }
      }

      countMediatorLiveData.addSource(electionsRepository.getFinishedElectionsCount(date)) { value ->
        countMediatorLiveData.value = countMediatorLiveData.value.apply {
          this?.let {
            this[FINISHED_ELECTION_COUNT] = value
          }
        }
      }
    }
  }

  fun deleteUserData() {
    Coroutines.main {
      userRepository.deleteUser()
    }
  }

  fun doLogout() {
    authListener?.onStarted()
    Coroutines.main {
      try {
        userRepository.logout()
        authListener?.onSuccess()
      } catch (e: ApiException) {
        authListener?.onFailure(e.message!!)
      } catch (e: SessionExpirationException) {
        authListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }
}
