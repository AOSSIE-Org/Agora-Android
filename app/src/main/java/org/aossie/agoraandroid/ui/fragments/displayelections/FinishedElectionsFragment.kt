package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_finished_elections.view.rv_finished_elections
import kotlinx.android.synthetic.main.fragment_finished_elections.view.tv_empty_election
import kotlinx.android.synthetic.main.fragment_finished_elections.view.tv_something_went_wrong
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.show
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class FinishedElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

  private lateinit var rootView: View

  private val displayElectionViewModel: DisplayElectionViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mElections: ArrayList<Election>
  private lateinit var electionsAdapter: ElectionsAdapter

  private val onItemClicked = { _id: String ->
    val action =
      FinishedElectionsFragmentDirections.actionFinishedElectionsFragmentToElectionDetailsFragment(_id)
    Navigation.findNavController(rootView)
      .navigate(action)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_finished_elections, container, false)
    mElections = ArrayList()
    electionsAdapter = ElectionsAdapter(mElections as List<Election>, onItemClicked)
    rootView.rv_finished_elections.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = electionsAdapter
    }

    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    bindUI()
  }

  private fun bindUI() {
    Coroutines.main {
      try {
        val elections = displayElectionViewModel.finishedElections.await()
        elections.observe(
          requireActivity(),
          Observer {
            if (it != null) {
              addElections(it)
            }
          }
        )
      } catch (e: IllegalStateException) {
        rootView.tv_something_went_wrong.show()
      }
    }
  }

  private fun addElections(elections: List<Election>) {
    if (elections.isNotEmpty()) {
      mElections.addAll(elections)
      electionsAdapter.notifyDataSetChanged()
    } else {
      rootView.tv_empty_election.show()
    }
  }
}
