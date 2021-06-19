package org.aossie.agoraandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.databinding.ListItemSelectedCandidatesBinding
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.CandidateRecyclerAdapterCallback

class SelectedCandidateAdapter(
  private val candidates: ArrayList<String>,
  private val adapterCallback: CandidateRecyclerAdapterCallback
) : RecyclerView.Adapter<SelectedCandidateAdapter.SelectedCandidateViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SelectedCandidateViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val binding: ListItemSelectedCandidatesBinding =
      DataBindingUtil.inflate(li, layout.list_item_selected_candidates, parent, false)
    return SelectedCandidateViewHolder(binding)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: SelectedCandidateViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position], adapterCallback)

  inner class SelectedCandidateViewHolder(
    listItemSelectedCandidatesBinding: ListItemSelectedCandidatesBinding
  ) : ViewHolder(listItemSelectedCandidatesBinding.root) {
    val binding = listItemSelectedCandidatesBinding

    fun instantiate(
      candidate: String,
      adapterCallback: CandidateRecyclerAdapterCallback
    ) {
      binding.tvSelectedCandidateName.text = candidate
      itemView.setOnClickListener {
        adapterCallback.onItemClicked(
          candidate, binding.tvSelectedCandidateName, AppConstants.UPVOTED_CANDIDATE_ITEM_CLICKED
        )
      }
    }
  }
}
