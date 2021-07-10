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
import org.aossie.agoraandroid.adapters.VotersAdapter
import org.aossie.agoraandroid.data.dto.VotersDto
import org.aossie.agoraandroid.databinding.FragmentVotersBinding
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
class VotersFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(),
  SessionExpiredListener {

  private lateinit var binding: FragmentVotersBinding

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
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voters, container, false)
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

    return binding.root
  }

  private fun setObserver() {
    electionDetailsViewModel.getVoterResponseLiveData.observe(
      viewLifecycleOwner,
      { responseUI ->
        when (responseUI.status) {
          ResponseUI.Status.LOADING -> binding.progressBar.show()
          ResponseUI.Status.SUCCESS -> {
            binding.root.snackbar(responseUI.message ?: "")
            binding.progressBar.hide()
            responseUI.dataList?.let {
              initRecyclerView(it)
            } ?: binding.tvNoVotersForThisElection.show()
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
      {
        if (it) {
          getVotersFromDb()
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

  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
