package org.aossie.agoraandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.databinding.ListItemAddCandidateBinding
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
    val binding: ListItemAddCandidateBinding =
      DataBindingUtil.inflate(li, layout.list_item_add_candidate, parent, false)
    return CandidatesViewHolder(binding)
  }

  override fun getItemCount(): Int = candidates.size

  override fun onBindViewHolder(
    holder: CandidatesViewHolder,
    position: Int
  ) = holder.instantiate(candidates[position], adapterCallback)

  inner class CandidatesViewHolder(
    listItemAddCandidateBinding: ListItemAddCandidateBinding
  ) : ViewHolder(listItemAddCandidateBinding.root) {
    val binding = listItemAddCandidateBinding

    fun instantiate(
      candidate: String,
      adapterCallback: CandidateRecyclerAdapterCallback
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
          adapterCallback.onItemClicked(
            candidate, binding.tvCandidateName, AppConstants.CANDIDATE_ITEM_CLICKED
          )
        }
      }
    }
  }
}
