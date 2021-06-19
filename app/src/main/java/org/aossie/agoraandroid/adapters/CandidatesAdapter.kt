package org.aossie.agoraandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.utilities.AppConstants
import org.aossie.agoraandroid.utilities.CandidateRecyclerAdapterCallback

class CandidatesAdapter(
  private val candidates: ArrayList<String>,
  private val isActive: Boolean,
  private val adapterCallback: CandidateRecyclerAdapterCallback
) : RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CandidatesViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val candidateView = li.inflate(layout.list_item_add_candidate, parent, false)
    return CandidatesViewHolder(candidateView, isActive)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: CandidatesViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position], adapterCallback)

  class CandidatesViewHolder(
    itemView: View,
    isActive1: Boolean
  ) : ViewHolder(itemView) {
    private val isActive = isActive1
    fun instantiate(
      candidate: String,
      adapterCallback: CandidateRecyclerAdapterCallback
    ) {
      val textView: TextView = itemView.findViewById(id.tv_candidate_name)
      textView.text = candidate
      if (isActive) {
        itemView.setOnClickListener {
          adapterCallback.onItemClicked(candidate, textView, AppConstants.CANDIDATE_ITEM_CLICKED)
        }
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_plus, 0)
      } else {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
      }
    }
  }
}
