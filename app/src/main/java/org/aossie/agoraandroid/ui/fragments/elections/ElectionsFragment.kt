package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.databinding.FragmentElectionsBinding
import org.aossie.agoraandroid.utilities.ElectionRecyclerAdapterCallback
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import javax.inject.Inject

class ElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(),
  ElectionRecyclerAdapterCallback {

  private lateinit var binding: FragmentElectionsBinding

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mElections: ArrayList<Election>
  private lateinit var electionsAdapter: ElectionsAdapter

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_elections, container, false)
    mElections = ArrayList()
    electionsAdapter = ElectionsAdapter(mElections as List<Election>, this)
    binding.rvTotalElections.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = electionsAdapter
    }
    binding.searchView.setOnQueryChangeListener { _, query ->
      filter(query)
    }
    val leftActionIcon =
      binding.searchView.findViewById<View>(com.arlib.floatingsearchview.R.id.left_action)

    return binding.root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    bindUI()
  }

  private fun bindUI() {
    try {
      electionViewModel.getElections()
        .observe(
          viewLifecycleOwner,
          Observer {
            if (it != null) {
              addElections(it)
            }
          }
        )
    } catch (e: IllegalStateException) {
      binding.tvSomethingWentWrong.show()
    }
  }

  private fun addElections(elections: List<Election>) {
    if (elections.isNotEmpty()) {
      mElections.addAll(elections)
      electionsAdapter.notifyDataSetChanged()
    } else {
      binding.tvEmptyElection.show()
    }
  }

  private fun filter(query: String) {
    val updatedList: java.util.ArrayList<Election> = java.util.ArrayList()
    for (election in mElections) {
      if (election.name?.contains(query) == true || election.description?.contains(query) == true) {
        updatedList.add(election)
      }
    }
    if (updatedList.isEmpty()) {
      binding.tvEmptyElection.show()
    } else {
      binding.tvEmptyElection.hide()
    }
    electionsAdapter.updateList(updatedList)
  }

  override fun onItemClicked(_id: String) {
    val action =
      ElectionsFragmentDirections
        .actionElectionsFragmentToElectionDetailsFragment(_id)
    Navigation.findNavController(binding.root)
      .navigate(action)
  }
}
