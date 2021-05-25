package org.aossie.agoraandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R.id
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter.VoterViewHolder

class VoterRecyclerAdapter(
  private val voterNameList: List<String>,
  private val voterEmailList: List<String>
) : Adapter<VoterViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): VoterViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = li.inflate(layout.list_item_voter_details, parent, false)
    return VoterViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: VoterViewHolder,
    position: Int
  ) {
    holder.voterName.text = voterNameList.get(position)
    holder.voterEmail.text = voterEmailList.get(position)
  }

  override fun getItemCount(): Int {
    return voterEmailList.size
  }

  class VoterViewHolder(itemView: View) : ViewHolder(itemView) {
    val voterName: TextView = itemView.findViewById(id.text_view_voter_name)
    val voterEmail: TextView = itemView.findViewById(id.text_view_voters_email)
  }
}
