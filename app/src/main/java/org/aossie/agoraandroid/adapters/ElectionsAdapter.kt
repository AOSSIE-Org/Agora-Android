package org.aossie.agoraandroid.adapters

import android.content.Context
import android.os.Build.VERSION_CODES
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.utilities.ElectionRecyclerAdapterCallback
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.databinding.ListItemElectionsBinding
import java.lang.StringBuilder
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val ACTIVE_ELECTION_LABEL = "ACTIVE"
private const val PENDING_ELECTION_LABEL = "PENDING"
private const val FINISHED_ELECTION_LABEL = "FINISHED"

class ElectionsAdapter(
  private val elections: List<Election>,
  private val adapterCallback: ElectionRecyclerAdapterCallback
) : RecyclerView.Adapter<ElectionsAdapter.ElectionsViewHolder>() {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ElectionsViewHolder {
    val binding: ListItemElectionsBinding = DataBindingUtil.inflate(
        LayoutInflater.from(parent.context),
        R.layout.list_item_elections, parent, false
    )
    return ElectionsViewHolder(binding, parent.context)
  }

  override fun getItemCount(): Int = elections.size
  
  override fun onBindViewHolder(
    holder: ElectionsViewHolder,
    position: Int
  ) = holder.bind(elections[position], adapterCallback)

  class ElectionsViewHolder(
    val binding: ListItemElectionsBinding,
    val context: Context
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(election: Election, adapterCallback: ElectionRecyclerAdapterCallback) {
      binding.election = election
      try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val formattedStartingDate: Date? = formatter.parse(election.start!!)
        val formattedEndingDate: Date? = formatter.parse(election.end!!)
        val currentDate = Calendar.getInstance()
            .time
        val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)
        //set end and start date
        binding.tvEndDate.text = outFormat.format(formattedEndingDate!!)
        binding.tvStartDate.text = outFormat.format(formattedStartingDate!!)
        //set label color and election status
        if (currentDate.before(formattedStartingDate)) {
          binding.label.text = PENDING_ELECTION_LABEL
          binding.label.setBackgroundResource(R.drawable.pending_election_label)
        } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
          binding.label.text = ACTIVE_ELECTION_LABEL
          binding.label.setBackgroundResource(R.drawable.active_election_label)
        } else if (currentDate.after(formattedEndingDate)) {
          binding.label.text = FINISHED_ELECTION_LABEL
          binding.label.setBackgroundResource(R.drawable.finished_election_label)
        }
      }catch (e: ParseException){
        e.printStackTrace()
      }
      // add candidates name
      val mCandidatesName = StringBuilder()
      val candidates = election.candidates
      if (candidates != null) {
        for (j in 0 until candidates.size) {
          mCandidatesName.append(candidates[j])
          if (j != candidates.size - 1) {
            mCandidatesName.append(", ")
          }
        }
      }
      binding.tvCandidateList.text = mCandidatesName
      binding.executePendingBindings()
      itemView.setOnClickListener {
        adapterCallback.onItemClicked(election._id)
      }
    }
  }
}