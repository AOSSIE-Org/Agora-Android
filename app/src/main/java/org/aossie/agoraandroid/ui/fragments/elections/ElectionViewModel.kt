package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
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
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.ElectionsUseCases
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import org.aossie.agoraandroid.utilities.AppConstants
import java.time.LocalDate
import java.util.Locale
import javax.inject.Inject

@RequiresApi(VERSION_CODES.O)
class ElectionViewModel
@Inject
constructor(
  private val electionsUseCases: ElectionsUseCases
) : ViewModel() {

  val elections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val search = mutableStateOf("")

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

  private val _currentDateState = mutableStateOf(LocalDate.now())
  val currentDateState: State<LocalDate> = _currentDateState

  init {
    viewModelScope.launch {
      electionsUseCases.fetchAndSaveElection()
    }
  }

  fun getElectionsForDate(date: LocalDate) = viewModelScope.launch {
    _currentDateState.value = date
    showLoading("Loading...")
    electionsUseCases.getElections().collect {
      elections.value = filterElectionsByDate(date,it)
      hideLoading()
    }
  }

  private fun filterElectionsByDate(date: LocalDate, elections: List<ElectionModel>): List<ElectionModel> {
    return elections.filter { election ->
      val startDate = election.start?.substringBefore("T")
      val endDate = election.end?.substringBefore("T")
      val electionStartDate = LocalDate.parse(startDate)
      val electionEndDate = LocalDate.parse(endDate)
      electionStartDate <= date && electionEndDate >= date
    }
  }

  fun getElectionsState(query:String){
    viewModelScope.launch {
      try {
        electionsUseCases.getElections().collectLatest { list ->
          search.value = query
          if(query.isEmpty()) {
            elections.emit(list)
          }else{
            elections.emit(filter(list, query))
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
