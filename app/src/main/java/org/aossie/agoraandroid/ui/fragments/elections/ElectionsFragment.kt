package org.aossie.agoraandroid.ui.fragments.elections

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
import kotlinx.android.synthetic.main.fragment_elections.view.rv_total_elections
import org.aossie.agoraandroid.ElectionAdapterCallback
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.ElectionsAdapter
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.utilities.Coroutines
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class ElectionsFragment
@Inject
constructor(
  private val viewModelFactory: ViewModelProvider.Factory
) : Fragment(), ElectionAdapterCallback {

  private lateinit var rootView: View

  private val electionViewModel: ElectionViewModel by viewModels {
    viewModelFactory
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_elections, container, false)
    return rootView
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    bindUI()
  }

  private fun bindUI() {
    Coroutines.main {
      val elections = electionViewModel.elections.await()
      elections.observe(activity!!, Observer {
        initRecyclerView(it)
      })
    }
  }

  private fun initRecyclerView(elections: List<Election>) {
    val electionsAdapter = ElectionsAdapter(elections)
    rootView.rv_total_elections.apply {
      layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
      adapter = electionsAdapter
    }
  }

  override fun onItemClicked(
    electionName: String,
    electionDesc: String,
    startDate: String,
    endDate: String,
    status: String,
    candidate: String,
    id: String
  ) {

  }

}
