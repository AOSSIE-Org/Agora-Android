package org.aossie.agoraandroid.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.layout
import org.aossie.agoraandroid.data.adapters.BallotsAdapter.BallotsViewHolder
import org.aossie.agoraandroid.domain.model.BallotDtoModel

class BallotsAdapter(private val ballots: List<BallotDtoModel>) : Adapter<BallotsViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): BallotsViewHolder {
    val li = parent.context
      .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = li.inflate(layout.list_item_ballot_details, parent, false)
    return BallotsViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: BallotsViewHolder,
    position: Int
  ) {
    holder.voteBallot.text = ballots[position].voteBallot
    holder.voterEmail.text = ballots[position].hash
  }

  override fun getItemCount(): Int {
    return ballots.size
  }

  class BallotsViewHolder(itemView: View) : ViewHolder(itemView) {
    val voteBallot: TextView = itemView.findViewById(R.id.text_view_vote_ballot)
    val voterEmail: TextView = itemView.findViewById(R.id.text_view_voter_email)
  }
}
