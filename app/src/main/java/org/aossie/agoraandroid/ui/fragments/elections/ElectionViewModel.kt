package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
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
      it.name?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true ||
        it.description?.lowercase(Locale.ROOT)?.contains(query.lowercase(Locale.ROOT)) == true
    }
  }
}
