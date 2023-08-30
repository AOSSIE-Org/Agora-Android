package org.aossie.agoraandroid.ui.fragments.displayelections

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.displayElection.DisplayElectionsUseCases
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.lazyDeferred
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DisplayElectionViewModel
@Inject
constructor(
  private val displayElectionsUseCases: DisplayElectionsUseCases
) : ViewModel() {

  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
    .time
  private val date: String = formatter.format(currentDate)

  val activeElections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val pendingElections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val finishedElections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val search = mutableStateOf("")

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  fun getFinishedElectionsState(query:String){
    viewModelScope.launch {
      try {
        displayElectionsUseCases.getFinishedElections(date).collectLatest { list ->
          search.value = query
          if(query.isEmpty()) {
            finishedElections.emit(list)
          }else{
            finishedElections.emit(filter(list, query))
          }
        }
      } catch (e: IllegalStateException) {
        showMessage(R.string.something_went_wrong_please_try_again_later)
      }
    }
  }

  fun getActiveElectionsState(query:String) {
    viewModelScope.launch {
      try {
        displayElectionsUseCases.getActiveElections(date).collectLatest { list ->
          search.value = query
          if(query.isEmpty()) {
            activeElections.emit(list)
          }else{
            activeElections.emit(filter(list, query))
          }
        }
      } catch (e: IllegalStateException) {
        showMessage(R.string.something_went_wrong_please_try_again_later)
      }
    }
  }

  fun getPendingElectionsState(query:String) {
    viewModelScope.launch {
      try {
        displayElectionsUseCases.getPendingElections(date).collectLatest { list ->
          search.value = query
          if(query.isEmpty()) {
            pendingElections.emit(list)
          }else{
            pendingElections.emit(filter(list, query))
          }
        }
      } catch (e: IllegalStateException) {
        showMessage(R.string.something_went_wrong_please_try_again_later)
      }
    }
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
  
  val finishedElections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val search = mutableStateOf("")

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  fun getFinishedElectionsState(query:String){
    viewModelScope.launch {
      try {
        displayElectionsUseCases.getFinishedElections(date).collectLatest { list ->
          search.value = query
          if(query.isEmpty()) {
            finishedElections.emit(list)
          }else{
            finishedElections.emit(filter(list, query))
          }
        }
      } catch (e: IllegalStateException) {
        showMessage(R.string.something_went_wrong_please_try_again_later)
      }
    }
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

  fun filter(
    mElections: List<ElectionModel>,
    query: String
  ): List<ElectionModel> {
    return mElections.filter {
      it.name?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true ||
        it.description?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true
    }
  }
}
