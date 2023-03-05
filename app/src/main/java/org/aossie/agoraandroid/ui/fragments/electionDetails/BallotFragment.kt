package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.adapters.BallotsAdapter
import org.aossie.agoraandroid.databinding.FragmentBallotBinding
import org.aossie.agoraandroid.domain.model.BallotDtoModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class BallotFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null
  private lateinit var binding: FragmentBallotBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentBallotBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {

    binding.tvEmptyBallots.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    binding.recyclerViewBallots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<BallotDtoModel>()
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

    lifecycleScope.launch {
      electionDetailsViewModel.getBallotResponseStateFlow.collect { responseUI ->
        if (responseUI != null) {
          when (responseUI.status) {
            ResponseUI.Status.LOADING -> binding.progressBar.hide()
            ResponseUI.Status.SUCCESS -> {
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
      }
    }
  }

  private fun initRecyclerView(ballots: List<BallotDtoModel>) {
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
    lifecycleScope.launch {
      electionDetailsViewModel.getElectionById(id!!)
        .collect {
          initRecyclerView(it.ballot as List<BallotDtoModel>)
          binding.progressBar.hide()
        }
    }
  }
}
