package org.aossie.agoraandroid.ui.fragments.elections

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.utilities.lazyDeferred
import javax.inject.Inject

class ElectionViewModel
  @Inject
  constructor(
    private val electionsRepository: ElectionsRepository,
    private val prefs: PreferenceProvider
  ): ViewModel(){

  val elections by lazyDeferred {
    electionsRepository.getElections()
  }
}