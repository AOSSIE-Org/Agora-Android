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
import org.aossie.agoraandroid.adapters.VotersAdapter.VotersViewHolder
import org.aossie.agoraandroid.data.db.model.VoterList

class VotersAdapter(private val voters: List<VoterList>) : Adapter<VotersViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): VotersViewHolder {
    val li = parent.context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val voterView = li.inflate(layout.list_item_voter_details, parent, false)
    return VotersViewHolder(voterView)
  }

  override fun onBindViewHolder(
    holder: VotersViewHolder,
    position: Int
  ) {
    holder.voterName.text = voters[position].name
    holder.voterEmail.text = voters[position].hash
  }

  override fun getItemCount(): Int {
    return voters.size
  }

  class VotersViewHolder(itemView: View) :
      ViewHolder(itemView) {
    val voterName: TextView = itemView.findViewById(id.text_view_voter_name)
    val voterEmail: TextView = itemView.findViewById(id.text_view_voters_email)
  }

}
