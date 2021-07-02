package org.aossie.agoraandroid.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.SessionExpirationException
import org.aossie.agoraandroid.utilities.lazyDeferred
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class HomeViewModel @Inject
constructor(
  private val electionsRepository: ElectionsRepository,
  private val userRepository: UserRepository
) : ViewModel() {
  private val _getLogoutLiveData: MutableLiveData<ResponseUI<Any>> = MutableLiveData()
  val getLogoutLiveData = _getLogoutLiveData
  var sessionExpiredListener: SessionExpiredListener ? = null
  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
    .time
  private val date: String = formatter.format(currentDate)

  val totalElectionsCount by lazyDeferred {
    electionsRepository.getTotalElectionsCount()
  }
  val pendingElectionsCount by lazyDeferred {
    electionsRepository.getPendingElectionsCount(date)
  }
  val finishedElectionsCount by lazyDeferred {
    electionsRepository.getFinishedElectionsCount(date)
  }
  val activeElectionsCount by lazyDeferred {
    electionsRepository.getActiveElectionsCount(date)
  }

  fun getElections(): LiveData<List<Election>> {
    GlobalScope.launch {
      electionsRepository.fetchAndSaveElections()
    }
    return electionsRepository.getElections()
  }

  fun deleteUserData() {
    Coroutines.main {
      userRepository.deleteUser()
    }
  }

  fun doLogout() {
   _getLogoutLiveData.value = ResponseUI.loading()
    Coroutines.main {
      try {
        userRepository.logout()
        _getLogoutLiveData.value = ResponseUI.success()
      } catch (e: ApiException) {
        _getLogoutLiveData.value = ResponseUI.error(e.message?:"")
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        _getLogoutLiveData.value = ResponseUI.error(e.message?:"")
      } catch (e: Exception) {
        _getLogoutLiveData.value = ResponseUI.error(e.message?:"")
      }
    }
  }
}
