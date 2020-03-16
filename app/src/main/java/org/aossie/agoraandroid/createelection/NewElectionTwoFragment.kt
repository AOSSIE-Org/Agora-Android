package org.aossie.agoraandroid.createelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_new_election_two.*
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter

class NewElectionTwoFragment(private val callingActivity: NewElectionActivity) : Fragment() {

  private var candidateRecyclerAdapter: CandidateRecyclerAdapter? = null
  private val itemTouchHelperCallback: SimpleCallback =
    object : SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(
        viewHolder: ViewHolder,
        direction: Int
      ) {
        callingActivity.mCandidateList.removeAt(viewHolder.adapterPosition)
        candidateRecyclerAdapter!!.notifyDataSetChanged()
      }
    }
  private val mAddCandidateTextInput: TextInputLayout? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_new_election_two, container, false)
  }

  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?
  ) {
    candidateRecyclerAdapter = CandidateRecyclerAdapter(callingActivity.mCandidateList)
    names_rv.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(names_rv)
    names_rv.adapter = candidateRecyclerAdapter
    submit_details_btn.setOnClickListener {
      if(callingActivity.mCandidateList.isEmpty()) {
        Toast.makeText(context, "Please add at least 1 candidate.", Toast.LENGTH_SHORT).show()
      } else {
        callingActivity.nextStep()
      }
    }
    add_candidate_btn.setOnClickListener {
      val name: String = candidate_til!!.editText!!
          .text
          .toString()
          .trim { it <= ' ' }
      addCandidate(name)
    }
  }

  private fun addCandidate(cName: String) {
    callingActivity.mCandidateList.add(cName)
    candidateRecyclerAdapter!!.notifyDataSetChanged()
    candidate_til!!.editText!!.setText("")
  }

}
