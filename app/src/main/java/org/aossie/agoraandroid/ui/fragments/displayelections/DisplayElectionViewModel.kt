package org.aossie.agoraandroid.ui.fragments.displayelections

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.displayElection.DisplayElectionsUseCases
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
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
  val pendingElections by lazyDeferred {
    displayElectionsUseCases.getPendingElections(date)
  }
  val finishedElections by lazyDeferred {
    displayElectionsUseCases.getFinishedElections(date)
  }
  val search = mutableStateOf("")

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

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
        showMessageResource(R.string.something_went_wrong_please_try_again_later)
      }
    }
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
