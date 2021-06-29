package org.aossie.agoraandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.databinding.ListItemAddCandidateBinding

class CandidatesAdapter(
  private val candidates: ArrayList<String>,
  private val isActive: Boolean,
  private val onItemClicked: (name: String) -> Unit
) : RecyclerView.Adapter<CandidatesAdapter.CandidatesViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): CandidatesViewHolder {
    val binding: ListItemAddCandidateBinding =
      DataBindingUtil.inflate(
        LayoutInflater.from(parent.context), layout.list_item_add_candidate, parent, false
      )
    return CandidatesViewHolder(binding, onItemClicked)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: CandidatesViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position])

  inner class CandidatesViewHolder(
    private val binding: ListItemAddCandidateBinding,
    private val onItemClicked: (name: String) -> Unit
  ) : ViewHolder(binding.root) {

    fun instantiate(
      candidate: String,
    ) {
      binding.tvCandidateName.apply {
        text = candidate
        setCompoundDrawablesWithIntrinsicBounds(
          0,
          0,
          if (isActive) R.drawable.ic_plus
          else 0,
          0
        )
      }
      itemView.setOnClickListener {
        if (isActive) {
          onItemClicked(
            candidate
          )
        }
      }
    }
  }
}
