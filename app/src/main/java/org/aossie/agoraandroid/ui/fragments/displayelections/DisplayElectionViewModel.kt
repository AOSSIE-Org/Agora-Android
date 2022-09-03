package org.aossie.agoraandroid.ui.fragments.displayelections

import androidx.lifecycle.ViewModel
import org.aossie.agoraandroid.domain.model.ElectionModel
import org.aossie.agoraandroid.domain.useCases.displayElection.DisplayElectionsUseCases
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

  val activeElections by lazyDeferred {
    displayElectionsUseCases.getActiveElectionsUseCase(date)
  }
  val pendingElections by lazyDeferred {
    displayElectionsUseCases.getPendingElectionsUseCase(date)
  }
  val finishedElections by lazyDeferred {
    displayElectionsUseCases.getFinishedElectionsUseCase(date)
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
