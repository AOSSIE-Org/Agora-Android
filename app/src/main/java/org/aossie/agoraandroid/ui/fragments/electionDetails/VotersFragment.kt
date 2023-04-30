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
import org.aossie.agoraandroid.data.adapters.VotersAdapter
import org.aossie.agoraandroid.databinding.FragmentVotersBinding
import org.aossie.agoraandroid.domain.model.VotersDtoModel
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.ResponseUI
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class VotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private lateinit var binding: FragmentVotersBinding

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding = FragmentVotersBinding.inflate(inflater)
    return binding.root
  }

  override fun onFragmentInitiated() {

    binding.tvNoVotersForThisElection.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    binding.recyclerViewVoters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<VotersDtoModel>()
      adapter = VotersAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getVoter(id)
    setObserver()
  }

  override fun onNetworkConnected() {
    electionDetailsViewModel.getVoter(id)
  }

  private fun setObserver() {
    lifecycleScope.launch {
      electionDetailsViewModel.getVoterResponseStateFlow.collect { responseUI ->
        if (responseUI != null) {
          when (responseUI.status) {
            ResponseUI.Status.LOADING -> binding.progressBar.show()
            ResponseUI.Status.SUCCESS -> {
              notify(responseUI.message ?: "")
              binding.progressBar.hide()
              responseUI.dataList?.let {
                initRecyclerView(it)
              } ?: binding.tvNoVotersForThisElection.show()
            }
            ResponseUI.Status.ERROR -> {
              if (responseUI.message == getString(R.string.no_network)) getVotersFromDb()
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

  private fun initRecyclerView(voters: List<VotersDtoModel>) {
    if (voters.isEmpty()) {
      binding.tvNoVotersForThisElection.show()
    }
    val votersAdapter = VotersAdapter(voters)
    binding.recyclerViewVoters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = votersAdapter
    }
  }

  private fun getVotersFromDb() {
    lifecycleScope.launch {
      electionDetailsViewModel.getElectionById(id!!)
        .collect {
          initRecyclerView(it.voterList as List<VotersDtoModel>)
          binding.progressBar.hide()
        }
    }
  }
}
