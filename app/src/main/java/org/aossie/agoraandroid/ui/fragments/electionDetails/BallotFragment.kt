package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_ballot.view.progress_bar
import kotlinx.android.synthetic.main.fragment_ballot.view.recycler_view_ballots
import kotlinx.android.synthetic.main.fragment_ballot.view.tv_empty_ballots
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.BallotsAdapter
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import org.aossie.agoraandroid.utilities.snackbar
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(),
  SessionExpiredListener {

  private lateinit var rootView: View

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private val hostViewModel: MainActivityViewModel by activityViewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_ballot, container, false)

    rootView.tv_empty_ballots.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    rootView.recycler_view_ballots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<BallotDto>()
      adapter = BallotsAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getBallot(id)

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {

      electionDetailsViewModel.getBallotResponseLiveData.observe(
        viewLifecycleOwner,
        { responseUI ->
          when (responseUI.status) {
            ResponseUI.Status.LOADING -> rootView.progress_bar.hide()
            ResponseUI.Status.SUCCESS -> {
              if (responseUI.message.isNullOrBlank()) rootView.snackbar(responseUI.message ?: "")
              rootView.progress_bar.hide()

              responseUI.dataList?.let {
                initRecyclerView(it)
              } ?: rootView.tv_empty_ballots.show()
            }
            ResponseUI.Status.ERROR -> {
              rootView.snackbar(responseUI.message ?: "")
              rootView.progress_bar.hide()
            }
          }
        }
      )

      electionDetailsViewModel.notConnected.observe(
        viewLifecycleOwner,
        Observer {
          if (it) {
            getBallotsFromDb()
          }
        }
      )
    }
  }

  private fun initRecyclerView(ballots: List<BallotDto>) {
    if (ballots.isEmpty()) {
      rootView.tv_empty_ballots.show()
    }
    val ballotsAdapter = BallotsAdapter(ballots)
    rootView.recycler_view_ballots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = ballotsAdapter
    }
  }

  private fun getBallotsFromDb() {
    Coroutines.main {
      electionDetailsViewModel.getElectionById(id!!).observe(
        viewLifecycleOwner,
        Observer {
          initRecyclerView(it.ballot as List<BallotDto>)
          rootView.progress_bar.hide()
        }
      )
    }
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
