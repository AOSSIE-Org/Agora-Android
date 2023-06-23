package org.aossie.agoraandroid.ui.fragments.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yariksoffice.lingver.Lingver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.useCases.homeFragment.HomeFragmentUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.LocaleUtil
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
  private val homeViewModelUseCases: HomeFragmentUseCases,
  private val prefs: PreferenceProvider
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

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  private val _uiEvents = MutableSharedFlow<UiEvents>()
  val uiEvents = _uiEvents.asSharedFlow()

  val appLanguage = prefs.getAppLanguage()
  val getSupportedLanguages = LocaleUtil.getSupportedLanguages()

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
    showLoading("Logging you out...")
    _getLogoutStateFLow.value = ResponseUI.loading()
    viewModelScope.launch {
      try {
        homeViewModelUseCases.logOut()
        hideSnackBar()
        hideLoading()
        _uiEvents.emit(UiEvents.UserLoggedOut)
        _getLogoutStateFLow.value = ResponseUI.success()
      } catch (e: ApiException) {
        showMessage(e.message!!)
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      } catch (e: Exception) {
        showMessage(e.message!!)
        _getLogoutStateFLow.value = ResponseUI.error(e.message)
      }
    }
  }

  fun changeLanguage(newLanguage: Pair<String, String>, context: Context) {
    viewModelScope.launch {
      prefs.updateAppLanguage(newLanguage.second)
      Lingver.getInstance().setLocale(context, newLanguage.second)
      delay(500)
      restartApp(context)
    }
  }

  private fun restartApp(context: Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
  }

  private fun showLoading(message: String?) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      isLoading = Pair(message!!,true)
    )
  }

  fun showMessage(message: String) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      error = Pair(message,true),
      isLoading = Pair("",false),
      errorResource = Pair(0,false)
    )
  }

  fun showMessageResource(messageResource: Int) {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      error = Pair("",false),
      isLoading = Pair("",false),
      errorResource = Pair(messageResource,true)
    )
  }

  fun hideSnackBar() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      error = Pair("",false),
      errorResource = Pair(0,false)
    )
  }

  fun hideLoading() {
    _progressAndErrorState.value=progressAndErrorState.value.copy(
      isLoading = Pair("",false)
    )
  }

  sealed class UiEvents{
    object UserLoggedOut:UiEvents()
  }
}
