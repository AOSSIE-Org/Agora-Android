package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.BallotsAdapter
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.databinding.FragmentBallotBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.utilities.Coroutines
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
  DisplayElectionListener {

  private lateinit var binding: FragmentBallotBinding

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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ballot, container, false)

    binding.tvEmptyBallots.hide()
    electionDetailsViewModel.displayElectionListener = this

    binding.recyclerViewBallots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<BallotDto>()
      adapter = BallotsAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getBallot(id)

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {
      electionDetailsViewModel.ballotResponse.observe(
        viewLifecycleOwner,
        Observer {
          if (it != null) {
            initRecyclerView(it)
          } else {
            binding.tvEmptyBallots.show()
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
      binding.tvEmptyBallots.show()
    }
    val ballotsAdapter = BallotsAdapter(ballots)
    binding.recyclerViewBallots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = ballotsAdapter
    }
  }

  private fun getBallotsFromDb() {
    Coroutines.main {
      electionDetailsViewModel.getElectionById(id!!)
        .observe(
          viewLifecycleOwner,
          Observer {
            initRecyclerView(it.ballot as List<BallotDto>)
            binding.progressBar.hide()
          }
        )
    }
  }

  override fun onDeleteElectionSuccess() {
    // do nothing
  }

  override fun onSuccess(message: String?) {
    if (message != null) binding.root.snackbar(message)
    binding.progressBar.hide()
  }

  override fun onStarted() {
    binding.progressBar.show()
  }

  override fun onFailure(message: String) {
    binding.root.snackbar(message)
    binding.progressBar.hide()
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
