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
import org.aossie.agoraandroid.adapters.BallotsAdapter
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.databinding.FragmentBallotBinding
import org.aossie.agoraandroid.ui.activities.main.MainActivityViewModel
import org.aossie.agoraandroid.ui.fragments.auth.SessionExpiredListener
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
  ): View {
    // Inflate the layout for this fragment
    binding = FragmentBallotBinding.inflate(layoutInflater)

    binding.tvEmptyBallots.hide()
    electionDetailsViewModel.sessionExpiredListener = this

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

    electionDetailsViewModel.getBallotResponseLiveData.observe(
      viewLifecycleOwner,
      { responseUI ->
        when (responseUI.status) {
          ResponseUI.Status.LOADING -> binding.progressBar.hide()
          ResponseUI.Status.SUCCESS -> {
            binding.root.snackbar(responseUI.message)
            binding.progressBar.hide()

            responseUI.dataList?.let {
              initRecyclerView(it)
            } ?: binding.tvEmptyBallots.show()
          }
          ResponseUI.Status.ERROR -> {
            binding.root.snackbar(responseUI.message)
            binding.progressBar.hide()
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
    electionDetailsViewModel.getElectionById(id!!)
      .observe(
        viewLifecycleOwner,
        Observer {
          initRecyclerView(it.ballot as List<BallotDto>)
          binding.progressBar.hide()
        }
      )
  }

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
