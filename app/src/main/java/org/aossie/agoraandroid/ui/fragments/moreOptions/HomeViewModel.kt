package org.aossie.agoraandroid.ui.fragments.moreOptions

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
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
  lateinit var authListener: AuthListener
  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
      .time
  private val date: String = formatter.format(currentDate)

  val elections by lazyDeferred {
    electionsRepository.getElections()
  }
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

  fun deleteUserData(){
    Coroutines.main {
      userRepository.deleteUser()
    }
  }

  fun doLogout() {
    authListener.onStarted()
    Coroutines.main {
      try {
        userRepository.logout()
        authListener.onSuccess()
      } catch (e: ApiException) {
        authListener.onFailure(e.message!!)
      } catch (e: NoInternetException) {
        authListener.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener.onFailure(e.message!!)
      }
    }
  }

}