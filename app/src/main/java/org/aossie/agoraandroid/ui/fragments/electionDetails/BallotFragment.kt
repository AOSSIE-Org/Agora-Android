package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.BallotsAdapter
import org.aossie.agoraandroid.data.dto.BallotDto
import org.aossie.agoraandroid.databinding.FragmentBallotBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment<FragmentBallotBinding>(viewModelFactory) {

  override val bindingInflater: (LayoutInflater) -> FragmentBallotBinding
    get() = {
      FragmentBallotBinding.inflate(it)
    }

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onFragmentInitiated() {

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
  }

  override fun onNetworkConnected() {
    electionDetailsViewModel.getBallot(id)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {

      electionDetailsViewModel.getBallotResponseLiveData.observe(
        viewLifecycleOwner,
        { responseUI ->
          when (responseUI.status) {
            ResponseUI.Status.LOADING -> binding.progressBar.hide()
            ResponseUI.Status.SUCCESS -> {
              notify(responseUI.message)
              binding.progressBar.hide()

              responseUI.dataList?.let {
                initRecyclerView(it)
              } ?: binding.tvEmptyBallots.show()
            }
            ResponseUI.Status.ERROR -> {
              if (responseUI.message == getString(R.string.no_network)) getBallotsFromDb()
              else {
                notify(responseUI.message)
                binding.progressBar.hide()
              }
            }
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
}
