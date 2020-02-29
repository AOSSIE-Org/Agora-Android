package org.aossie.agoraandroid.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.databinding.CreateElectionTwoFragmentBinding
import org.aossie.agoraandroid.utilities.CandidatesListFactory
import org.aossie.agoraandroid.utilities.TinyDB
import java.util.*


class CreateElectionTwo : Fragment() {

    private var name: String? = null
    private val mCandidates = ArrayList<String>()
    private var tinydb: TinyDB? = null
    private var candidateRecyclerAdapter: CandidateRecyclerAdapter? = null
    private lateinit var viewModel: CreateElectionTwoViewModel
    private lateinit var binding: CreateElectionTwoFragmentBinding


    private val itemTouchHelperCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.removeCandidate(viewHolder.adapterPosition)
            candidateRecyclerAdapter!!.notifyDataSetChanged()

        }
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        var factor = CandidatesListFactory(mCandidates)
        viewModel = ViewModelProviders.of(this, factor).get(CreateElectionTwoViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.create_election_two_fragment, container, false)
        binding.vm = viewModel
        tinydb = TinyDB(context!!)
        candidateRecyclerAdapter = CandidateRecyclerAdapter()
        binding.namesRv.layoutManager = LinearLayoutManager(context)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.namesRv)
        candidateRecyclerAdapter!!.setCandidates(viewModel.getAllCandidates())
//        viewModel.getAllCandidatesLive().observe(viewLifecycleOwner, Observer<ArrayList<String>>(){
//
//        })

        binding.namesRv!!.adapter = candidateRecyclerAdapter


        binding.submitDetailsBtn.setOnClickListener({
            if (viewModel.getAllCandidates()!!.size != 0) {
                binding.candidateTil.editText!!.setText(" ")
                tinydb!!.putListString("Candidates", mCandidates)
                Navigation.findNavController(it).navigate(R.id.action_election2_to_election3)
            } else {
                Toast.makeText(
                        context, "Please Add At least One Candidate",
                        Toast.LENGTH_SHORT
                ).show()
            }
        })



        binding.addCandidateBtn?.setOnClickListener({
            name = binding.candidateTil.editText!!.text.toString().trim { it <= ' ' }
            if (!name!!.isEmpty()) {
                viewModel.addCandidate(name!!)
                candidateRecyclerAdapter!!.notifyDataSetChanged()
                binding.candidateTil.editText!!.setText(" ")
            } else {
                Toast.makeText(context, "Please enter Name", Toast.LENGTH_LONG).show()
            }
        })
        return binding.root

    }


}
