package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import java.util.Locale
import javax.inject.Inject

class ElectionViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  fun getElections(): LiveData<List<Election>> {
    viewModelScope.launch {
      electionsRepository.fetchAndSaveElections()
    }
    return electionsRepository.getElections()
  }

  fun filter(
    mElections: List<Election>,
    query: String
  ): List<Election> {
    return mElections.filter {
      it.name?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true ||
        it.description?.toLowerCase(Locale.ROOT)?.contains(query.toLowerCase(Locale.ROOT)) == true ||
        it.candidates?.any { candidate -> candidate.toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT)) } == true
    }
  }
}
