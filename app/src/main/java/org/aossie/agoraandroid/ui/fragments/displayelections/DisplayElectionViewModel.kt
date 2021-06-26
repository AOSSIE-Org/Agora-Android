package org.aossie.agoraandroid.ui.fragments.displayelections

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.data.Repository.ElectionsRepository
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.utilities.lazyDeferred
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DisplayElectionViewModel
@Inject
constructor(
  private val electionsRepository: ElectionsRepository
) : ViewModel() {

  private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
  private val currentDate: Date = Calendar.getInstance()
    .time
  private val date: String = formatter.format(currentDate)

  val activeElections by lazyDeferred {
    electionsRepository.getActiveElections(date)
  }
  val pendingElections by lazyDeferred {
    electionsRepository.getPendingElections(date)
  }
  val finishedElections by lazyDeferred {
    electionsRepository.getFinishedElections(date)
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
