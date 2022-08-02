package org.aossie.agoraandroid.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.data.adapters.CandidateRecyclerAdapter.CandidateViewHolder

class CandidateRecyclerAdapter(
  private val candidates: List<String>
) : Adapter<CandidateViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CandidateViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = li.inflate(layout.list_item_candidate_name, parent, false)
    return CandidateViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: CandidateViewHolder,
    position: Int
  ) {
    holder.candidateName.text = candidates[position]
  }

  override fun getItemCount(): Int {
    return candidates.size
  }

  class CandidateViewHolder(itemView: View) :
    ViewHolder(itemView) {
    val candidateName: TextView = itemView.findViewById(id.text_candidate_name)
  }
}
