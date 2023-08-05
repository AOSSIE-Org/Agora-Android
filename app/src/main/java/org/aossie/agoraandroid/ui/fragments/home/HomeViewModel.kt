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
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.domain.useCases.homeFragment.HomeFragmentUseCases
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.ApiException
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.LocaleUtil
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
  private val homeViewModelUseCases: HomeFragmentUseCases,
  private val prefs: PreferenceProvider
) : ViewModel() {
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
    _countMediatorLiveData.value = mutableMapOf(
      TOTAL_ELECTION_COUNT to 0,
      PENDING_ELECTION_COUNT to 0,
      FINISHED_ELECTION_COUNT to 0,
      ACTIVE_ELECTION_COUNT to 0
    )
    addSource()
  }

  fun getElections() {
    showLoading("Loading...")
    GlobalScope.launch {
     val response = homeViewModelUseCases.fetchAndSaveElection()
      response.elections.let {
        hideLoading()
      }
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
    viewModelScope.launch {
      try {
        homeViewModelUseCases.logOut()
        hideSnackBar()
        hideLoading()
        _uiEvents.emit(UiEvents.UserLoggedOut)
      } catch (e: ApiException) {
        showMessage(e.message!!)
      } catch (e: SessionExpirationException) {
        sessionExpiredListener?.onSessionExpired()
      } catch (e: NoInternetException) {
        showMessage(e.message!!)
      } catch (e: Exception) {
        showMessage(e.message!!)
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

  sealed class UiEvents{
    object UserLoggedOut:UiEvents()
  }

  private fun showLoading(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair(message,true)
    )
  }

  fun showMessage(message: Any) {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair(message,true),
      loading = Pair("",false)
    )
    viewModelScope.launch {
      delay(AppConstants.SNACKBAR_DURATION)
      hideSnackBar()
    }
  }

  private fun hideSnackBar() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      message = Pair("",false)
    )
  }

  private fun hideLoading() {
    _progressAndErrorState.value = progressAndErrorState.value.copy(
      loading = Pair("",false)
    )
  }
}
