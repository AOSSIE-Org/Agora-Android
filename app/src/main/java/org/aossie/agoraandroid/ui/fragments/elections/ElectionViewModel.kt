package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.electionsAndCalenderView.ElectionsUseCases
import java.util.Locale
import javax.inject.Inject

class ElectionViewModel
@Inject
constructor(
  private val electionsUseCases: ElectionsUseCases
) : ViewModel() {

  fun getElections(): LiveData<List<ElectionModel>> {
    viewModelScope.launch {
      electionsUseCases.fetchAndSaveElectionUseCase()
    }
    return electionsUseCases.getElectionsUseCase()
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
