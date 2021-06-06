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
  private val electionsRepository: ElectionsRepository,
  private val prefs: PreferenceProvider
) : ViewModel() {

  fun getElections(): LiveData<List<Election>> {
    viewModelScope.launch {
      electionsRepository.fetchAndSaveElections()
    }
    return electionsRepository.getElections()
  }
}
