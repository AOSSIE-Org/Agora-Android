package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import org.aossie.agoraandroid.data.db.model.Ballot
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
  ) : Fragment(), DisplayElectionListener {

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
    rootView = inflater.inflate(R.layout.fragment_ballot, container, false)

    rootView.tv_empty_ballots.hide()
    displayElectionViewModel.displayElectionListener = this

    rootView.recycler_view_ballots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      val arr = ArrayList<Ballot>()
      adapter = BallotsAdapter(arr)
    }

    id = VotersFragmentArgs.fromBundle(arguments!!).id
    displayElectionViewModel.getBallot(id)

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    Coroutines.main {
      displayElectionViewModel.ballotResponse.observe(
          requireActivity(), Observer {
        if(it != null) {
          initRecyclerView(it)
        }
        else{
          rootView.tv_empty_ballots.show()
        }
      })
      displayElectionViewModel.notConnected.observe(
          requireActivity(), Observer {
        if(it){
          getBallotsFromDb()
        }
      })
    }
  }

  private fun initRecyclerView(ballots: List<Ballot>){
    if(ballots.isEmpty()){
      rootView.tv_empty_ballots.show()
    }
    val ballotsAdapter = BallotsAdapter(ballots)
    rootView.recycler_view_ballots.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = ballotsAdapter
    }
  }

  private fun getBallotsFromDb(){
    Coroutines.main {
      displayElectionViewModel.getElectionById(id!!).observe(requireActivity(), Observer {
        initRecyclerView(it.ballot as List<Ballot>)
        rootView.progress_bar.hide()
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
