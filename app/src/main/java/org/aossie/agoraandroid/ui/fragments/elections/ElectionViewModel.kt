package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.entities.Election
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
      it.name?.contains(query) == true || it.description?.contains(query) == true
    }
  }
}
