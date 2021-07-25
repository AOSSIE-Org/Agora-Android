package org.aossie.agoraandroid.ui.fragments.electionDetails

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.VotersAdapter
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.databinding.FragmentVotersBinding
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
class VotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment<FragmentVotersBinding>(viewModelFactory) {

  override val bindingInflater: (LayoutInflater) -> FragmentVotersBinding
    get() = {
      FragmentVotersBinding.inflate(it)
    }

  private val electionDetailsViewModel: ElectionDetailsViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null

  override fun onFragmentInitiated() {

    binding.tvNoVotersForThisElection.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    binding.recyclerViewVoters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<VotersDto>()
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
    electionDetailsViewModel.getVoterResponseLiveData.observe(
      viewLifecycleOwner,
      { responseUI ->
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
    )
  }

  private fun initRecyclerView(voters: List<VotersDto>) {
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
    Coroutines.main {
      electionDetailsViewModel.getElectionById(id!!)
        .observe(
          viewLifecycleOwner,
          Observer {
            if (it != null) {
              initRecyclerView(it.voterList as List<VotersDto>)
              binding.progressBar.hide()
            }
          }
        )
    }
  }
}
