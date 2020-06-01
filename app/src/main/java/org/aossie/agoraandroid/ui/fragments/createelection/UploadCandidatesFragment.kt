package org.aossie.agoraandroid.ui.fragments.createelection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.add_candidate_btn
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.candidate_til
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.names_rv
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInActivity
import org.aossie.agoraandroid.utilities.TinyDB
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class UploadCandidatesFragment : Fragment() {

  private lateinit var rootView: View

  private val mCandidates = ArrayList<String>()
  private var tinydb: TinyDB? = null
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
        mCandidates.removeAt(viewHolder.adapterPosition)
        candidateRecyclerAdapter!!.notifyDataSetChanged()
      }
    }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    rootView = inflater.inflate(R.layout.fragment_upload_candidates, container, false)
    tinydb = TinyDB(activity?.application)

    candidateRecyclerAdapter = CandidateRecyclerAdapter(mCandidates)
    rootView.names_rv.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rootView.names_rv)
    rootView.names_rv.adapter = candidateRecyclerAdapter
    rootView.submit_details_btn.setOnClickListener {
      if (mCandidates.size != 0) {
        hideKeyboardInActivity(activity as AppCompatActivity)
        tinydb?.putListString("Candidates", mCandidates)
        Navigation.findNavController(rootView)
            .navigate(UploadCandidatesFragmentDirections.actionUploadCandidatesFragmentToUploadVotingAlgoFragment())
      } else {
        Toast.makeText(
            context, "Please Add At least One Candidate",
            Toast.LENGTH_SHORT
        )
            .show()
      }
    }
    rootView.add_candidate_btn.setOnClickListener {
      val name = rootView.candidate_til.editText?.text.toString().trim { it <= ' ' }
      addCandidate(name)
    }

    return rootView
  }

  private fun addCandidate(cName: String) {
    mCandidates.add(cName)
    candidateRecyclerAdapter!!.notifyDataSetChanged()
    rootView.candidate_til.editText?.setText("")
  }

}
