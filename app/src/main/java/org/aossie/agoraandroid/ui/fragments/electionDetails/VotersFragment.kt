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
import kotlinx.android.synthetic.main.fragment_voters.view.progress_bar
import kotlinx.android.synthetic.main.fragment_voters.view.recycler_view_voters
import kotlinx.android.synthetic.main.fragment_voters.view.tv_no_voters_for_this_election
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.string
import org.aossie.agoraandroid.adapters.VotersAdapter
import org.aossie.agoraandroid.data.dto.VotersDto
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
    rootView = inflater.inflate(R.layout.fragment_voters, container, false)
    rootView.tv_no_voters_for_this_election.hide()
    electionDetailsViewModel.sessionExpiredListener = this

    rootView.recycler_view_voters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<VotersDto>()
      adapter = VotersAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(
      requireArguments()
    ).id
    electionDetailsViewModel.getVoter(id)
    setObserver()

    return rootView
  }

  private fun setObserver() {
    electionDetailsViewModel.getVoterResponseLiveData.observe(viewLifecycleOwner, { responseUI ->
      when (responseUI.status) {
        ResponseUI.Status.LOADING ->  rootView.progress_bar.show()
        ResponseUI.Status.SUCCESS -> {
          if (responseUI.message.isNullOrBlank()) rootView.snackbar(responseUI.message?:"")
          rootView.progress_bar.hide()
          responseUI.dataList?.let {
            initRecyclerView(it)
          }?: rootView.tv_no_voters_for_this_election.show()
        }
        ResponseUI.Status.ERROR -> {
          rootView.snackbar(responseUI.message?:"")
          rootView.progress_bar.hide()
        }
      }
    })

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
      rootView.tv_no_voters_for_this_election.show()
    }
    val votersAdapter = VotersAdapter(voters)
    rootView.recycler_view_voters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = votersAdapter
    }
  }

  private fun getVotersFromDb() {
    Coroutines.main {
      electionDetailsViewModel.getElectionById(id!!).observe(
        viewLifecycleOwner,
        Observer {
          if (it != null) {
            initRecyclerView(it.voterList as List<VotersDto>)
            rootView.progress_bar.hide()
          }
        }
      )
    }
  }





  override fun onSessionExpired() {
    hostViewModel.setLogout(true)
  }
}
