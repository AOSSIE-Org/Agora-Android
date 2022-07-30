package org.aossie.agoraandroid.ui.fragments.elections

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.common.utilities.hide
import org.aossie.agoraandroid.common.utilities.show
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.databinding.FragmentElectionsBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import javax.inject.Inject

class ElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment(viewModelFactory) {

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mElections: ArrayList<Election>
  private lateinit var electionsAdapter: ElectionsAdapter

  private val onItemClicked = { _id: String ->
    val action = ElectionsFragmentDirections
      .actionElectionsFragmentToElectionDetailsFragment(_id)
    Navigation.findNavController(binding.root)
      .navigate(action)
  }
  private lateinit var binding: FragmentElectionsBinding

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentElectionsBinding.inflate(inflater)
    return binding.root
  }
  override fun onFragmentInitiated() {
    mElections = ArrayList()
    electionsAdapter = ElectionsAdapter(onItemClicked)
    binding.rvTotalElections.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = electionsAdapter
    }
    binding.searchView.doAfterTextChanged {
      filter(it.toString())
    }
  }

  override fun onNetworkConnected() {
    bindUI()
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
      electionsAdapter.submitList(elections)
    } else {
      binding.tvEmptyElection.show()
    }
  }

  private fun filter(query: String) {
    val updatedList = electionViewModel.filter(mElections, query)
    electionsAdapter.submitList(updatedList)
    if (updatedList.isEmpty()) {
      binding.tvEmptyElection.show()
    } else {
      binding.tvEmptyElection.hide()
    }
  }
}
