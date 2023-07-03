package org.aossie.agoraandroid.ui.fragments.elections

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.ElectionsUseCases
import org.aossie.agoraandroid.ui.screens.common.Util.ScreensState
import java.util.Locale
import javax.inject.Inject

class ElectionViewModel
@Inject
constructor(
  private val electionsUseCases: ElectionsUseCases
) : ViewModel() {

  val elections = MutableStateFlow<List<ElectionModel>>(emptyList())
  val search = mutableStateOf("")

  private val _progressAndErrorState = MutableStateFlow (ScreensState())
  val progressAndErrorState = _progressAndErrorState.asStateFlow()

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

  fun getElections(): Flow<List<ElectionModel>> {
    viewModelScope.launch {
      electionsUseCases.fetchAndSaveElection()
    }
    return electionsUseCases.getElections()
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
