package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.ui.screens.calendar.CalendarScreen
import org.aossie.agoraandroid.ui.screens.calendar.CalendarScreenEvent.DateSelected
import org.aossie.agoraandroid.ui.screens.calendar.CalendarScreenEvent.OnElectionClicked
import org.aossie.agoraandroid.ui.screens.calendar.CalendarScreenEvent.OnViewAllElectionClick
import org.aossie.agoraandroid.ui.theme.AgoraTheme
import java.time.LocalDate
import javax.inject.Inject

class CalendarViewElectionFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory,
  private val prefs: PreferenceProvider
) : BaseFragment(viewModelFactory) {

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  private lateinit var composeView: ComposeView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return ComposeView(requireContext()).also {
      composeView = it
    }
  }

  @RequiresApi(VERSION_CODES.O)
  override fun onFragmentInitiated() {
    composeView.setContent {

      val electionsState by electionViewModel.elections.collectAsState()
      val currentDateState by electionViewModel.currentDateState
      val progressErrorState by electionViewModel.progressAndErrorState.collectAsState()

      LaunchedEffect(key1 = Unit) {
        electionViewModel.getElectionsForDate(LocalDate.now())
      }

      AgoraTheme {
        CalendarScreen(progressErrorState, currentDateState, electionsState) { event ->
          when(event) {
            is DateSelected -> {
              electionViewModel.getElectionsForDate(event.date)
            }
            is OnElectionClicked -> {
              openElectionDetail(event.id)
            }
            OnViewAllElectionClick -> {
              findNavController().navigate(
                CalendarViewElectionFragmentDirections
                  .actionCalendarViewElectionFragmentToElectionsFragment()
              )
            }
          }
        }
      }
    }
  }

  private fun openElectionDetail(id: String) {
    val action =
      CalendarViewElectionFragmentDirections
        .actionCalendarViewElectionFragmentToElectionDetailsFragment(id)
    findNavController().navigate(action)
  }
}