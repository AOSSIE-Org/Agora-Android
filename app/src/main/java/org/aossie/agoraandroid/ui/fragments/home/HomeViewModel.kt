package org.aossie.agoraandroid.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.Repository.UserRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.NoInternetException
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
  var authListener: AuthListener ?= null
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
    GlobalScope.launch{
      electionsRepository.fetchAndSaveElections()
    }
    return electionsRepository.getElections()
  }

  fun deleteUserData(){
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
        authListener?.onFailure(e.message!!)
      }catch (e: NoInternetException) {
        authListener?.onFailure(e.message!!)
      } catch (e: Exception) {
        authListener?.onFailure(e.message!!)
      }
    }
  }

}