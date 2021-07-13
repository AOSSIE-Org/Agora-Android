package org.aossie.agoraandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.databinding.ListItemSelectedCandidatesBinding

class SelectedCandidateAdapter(
  private val candidates: ArrayList<String>,
  private val onItemClicked: (name: String) -> Unit
) : RecyclerView.Adapter<SelectedCandidateAdapter.SelectedCandidateViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SelectedCandidateViewHolder {
    val binding = ListItemSelectedCandidatesBinding.inflate(LayoutInflater.from(parent.context))
    return SelectedCandidateViewHolder(binding, onItemClicked)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: SelectedCandidateViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position])

  inner class SelectedCandidateViewHolder(
    private val binding: ListItemSelectedCandidatesBinding,
    private val onItemClicked: (name: String) -> Unit
  ) : ViewHolder(binding.root) {

    fun instantiate(
      candidate: String
    ) {
      binding.tvSelectedCandidateName.text = candidate
      itemView.setOnClickListener {
        onItemClicked(
          candidate
        )
      }
    }
  }
}
