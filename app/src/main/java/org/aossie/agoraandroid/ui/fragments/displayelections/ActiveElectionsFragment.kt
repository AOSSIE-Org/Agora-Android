package org.aossie.agoraandroid.ui.fragments.displayelections

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.databinding.FragmentActiveElectionsBinding
import org.aossie.agoraandroid.ui.fragments.BaseFragment
import org.aossie.agoraandroid.utilities.Coroutines
import org.aossie.agoraandroid.utilities.hide
import org.aossie.agoraandroid.utilities.show
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ActiveElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : BaseFragment<FragmentActiveElectionsBinding>(viewModelFactory) {

  override val bindingInflater: (LayoutInflater) -> FragmentActiveElectionsBinding
    get() = {
      FragmentActiveElectionsBinding.inflate(it)
    }

  private val displayElectionViewModel: DisplayElectionViewModel by viewModels {
    viewModelFactory
  }

  lateinit var mElections: ArrayList<Election>
  private lateinit var electionsAdapter: ElectionsAdapter

  private val onItemClicked = { _id: String ->
    val action =
      ActiveElectionsFragmentDirections.actionActiveElectionsFragmentToElectionDetailsFragment(_id)
    Navigation.findNavController(binding.root)
      .navigate(action)
  }

  override fun onFragmentInitiated() {

    mElections = ArrayList()
    electionsAdapter = ElectionsAdapter(onItemClicked)
    binding.rvActiveElections.apply {
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
    Coroutines.main {
      try {
        val elections = displayElectionViewModel.activeElections.await()
        elections.observe(
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
    val updatedList = displayElectionViewModel.filter(mElections, query)
    electionsAdapter.submitList(updatedList)
    if (updatedList.isEmpty()) {
      binding.tvEmptyElection.show()
    } else {
      binding.tvEmptyElection.hide()
    }
  }
}
