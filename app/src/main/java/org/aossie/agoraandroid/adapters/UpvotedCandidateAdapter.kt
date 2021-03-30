package org.aossie.agoraandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.CandidateRecyclerAdapterCallback

class UpvotedCandidateAdapter(
  private val candidates: ArrayList<String>,
  private val adapterCallback: CandidateRecyclerAdapterCallback
) : RecyclerView.Adapter<UpvotedCandidateAdapter.UpvotedCandidateViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): UpvotedCandidateViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val candidateView = li.inflate(layout.list_item_selected_candidates, parent, false)
    return UpvotedCandidateViewHolder(candidateView)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: UpvotedCandidateViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position], adapterCallback)

  class UpvotedCandidateViewHolder(itemView: View) : ViewHolder(itemView) {
    fun instantiate(candidate: String, adapterCallback: CandidateRecyclerAdapterCallback) {
      val textView: TextView = itemView.findViewById(id.tv_selected_candidate_name)
      textView.text = candidate
      itemView.setOnClickListener {
        adapterCallback.onItemClicked(candidate, textView, AppConstants.UPVOTED_CANDIDATE_ITEM_CLICKED)
      }
    }
  }
}
