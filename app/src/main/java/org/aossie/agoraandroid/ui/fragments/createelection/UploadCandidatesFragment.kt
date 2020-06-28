package org.aossie.agoraandroid.ui.fragments.createelection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.add_candidate_btn
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.candidate_til
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.et_candidate_name
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.names_rv
import kotlinx.android.synthetic.main.fragment_upload_candidates.view.submit_details_btn
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter
import org.aossie.agoraandroid.utilities.HideKeyboard.hideKeyboardInActivity
import org.aossie.agoraandroid.utilities.snackbar
import java.util.ArrayList
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 */
class UploadCandidatesFragment
  @Inject
  constructor(
    private val electionDetailsSharedPrefs: ElectionDetailsSharedPrefs
  ): Fragment() {

  private lateinit var rootView: View

  private val mCandidates = ArrayList<String>()
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

    candidateRecyclerAdapter = CandidateRecyclerAdapter(mCandidates)
    rootView.names_rv.layoutManager = LinearLayoutManager(context)
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rootView.names_rv)
    rootView.names_rv.adapter = candidateRecyclerAdapter
    rootView.submit_details_btn.setOnClickListener {
      if (mCandidates.isNotEmpty()) {
        hideKeyboardInActivity(activity as AppCompatActivity)
        electionDetailsSharedPrefs.saveCandidates(mCandidates)
        Navigation.findNavController(rootView)
            .navigate(UploadCandidatesFragmentDirections.actionUploadCandidatesFragmentToUploadVotingAlgoFragment())
      } else {
        rootView.snackbar("Please Add At least One Candidate")
      }
    }

    rootView.et_candidate_name.addTextChangedListener(textWatcher)

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

  private val textWatcher: TextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun afterTextChanged(s: Editable) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      val candidateNameInput: String = rootView.et_candidate_name.text
          .toString()
          .trim()
      rootView.add_candidate_btn.isEnabled = candidateNameInput.isNotEmpty()
    }
  }

}
