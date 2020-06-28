package org.aossie.agoraandroid.ui.fragments.displayelections

import android.R.layout
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_voters.view.progress_bar
import kotlinx.android.synthetic.main.fragment_voters.view.recycler_view_voters
import kotlinx.android.synthetic.main.fragment_voters.view.tv_no_voters_for_this_election
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.VotersAdapter
import org.aossie.agoraandroid.data.db.PreferenceProvider
import org.aossie.agoraandroid.data.db.model.VoterList
import org.aossie.agoraandroid.utilities.Coroutines
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
    private val viewModelFactory: ViewModelProvider.Factory,
      private val prefs: PreferenceProvider
  ): Fragment(), DisplayElectionListener {

  private lateinit var rootView: View

  private val displayElectionViewModel: DisplayElectionViewModel by viewModels {
    viewModelFactory
  }

  private var id: String? = null
  private var token: String? = null

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_voters, container, false)
    rootView.tv_no_voters_for_this_election.hide()
    displayElectionViewModel.displayElectionListener = this

    rootView.recycler_view_voters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<VoterList>()
      adapter = VotersAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(arguments!!).id
    token = prefs.getCurrentToken()
    displayElectionViewModel.getVoter(token, id)

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {
      displayElectionViewModel.voterResponse.observe(
          requireActivity(), Observer {
        if(it != null) {
          initRecyclerView(it)
        }
        else{
          rootView.tv_no_voters_for_this_election.show()
        }
      })
      displayElectionViewModel.notConnected.observe(
          requireActivity(), Observer {
        if(it){
          getVotersFromDb()
        }
      })
    }
  }

  private fun initRecyclerView(voters: List<VoterList>){
    if(voters.isEmpty()){
      rootView.tv_no_voters_for_this_election.show()
    }
    val votersAdapter = VotersAdapter(voters)
    rootView.recycler_view_voters.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = votersAdapter
    }
  }

  private fun getVotersFromDb(){
    Coroutines.main {
      displayElectionViewModel.getElectionById(id!!).observe(requireActivity(), Observer {
        if(it != null ) {
          initRecyclerView(it.voterList as List<VoterList>)
          rootView.progress_bar.hide()
        }
      })
    }
  }

  override fun onDeleteElectionSuccess() {
    //do nothing
  }

  override fun onSuccess(message: String?) {
    if(message!=null) rootView.snackbar(message)
    rootView.progress_bar.hide()
  }

  override fun onStarted() {
    rootView.progress_bar.show()
  }

  override fun onFailure(message: String) {
      rootView.snackbar(message)
      rootView.progress_bar.hide()
  }

}
