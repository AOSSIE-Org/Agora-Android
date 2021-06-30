package org.aossie.agoraandroid.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.aossie.agoraandroid.R
import org.aossie.agoraandroid.R.drawable
import org.aossie.agoraandroid.data.db.entities.Election
import org.aossie.agoraandroid.databinding.ListItemElectionsBinding
import org.aossie.agoraandroid.utilities.AppConstants
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ElectionsAdapter(
  private val onItemClicked: (name: String) -> Unit
) : ListAdapter<Election, ElectionsAdapter.ElectionsViewHolder>(ElectionItemDiffCallback) {

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ElectionsViewHolder {
    val binding: ListItemElectionsBinding = DataBindingUtil.inflate(
      LayoutInflater.from(parent.context),
      R.layout.list_item_elections, parent, false
    )
    return ElectionsViewHolder(binding, onItemClicked)
  }

  override fun onBindViewHolder(
    holder: ElectionsViewHolder,
    position: Int
  ) = holder.bind(getItem(position))

  class ElectionsViewHolder(
    private val binding: ListItemElectionsBinding,
    private val onItemClicked: (name: String) -> Unit
  ) : RecyclerView.ViewHolder(binding.root) {

    fun bind(election: Election) {
      binding.election = election
      try {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        val formattedStartingDate: Date? = formatter.parse(election.start!!)
        val formattedEndingDate: Date? = formatter.parse(election.end!!)
        val currentDate = Calendar.getInstance()
          .time
        val outFormat = SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss", Locale.ENGLISH)
        // set end and start date
        binding.tvEndDate.text = outFormat.format(formattedEndingDate!!)
        binding.tvStartDate.text = outFormat.format(formattedStartingDate!!)
        // set label color and election status
        binding.label.text = getEventStatus(currentDate, formattedStartingDate, formattedEndingDate)?.name
        binding.label.setBackgroundResource(
          getEventColor(currentDate, formattedStartingDate, formattedEndingDate)
        )
      } catch (e: ParseException) {
        e.printStackTrace()
      }
      // add candidates name
      val mCandidatesName = StringBuilder()
      val candidates = election.candidates
      if (candidates != null) {
        for (j in candidates.indices) {
          mCandidatesName.append(candidates[j])
          if (j != candidates.size - 1) {
            mCandidatesName.append(", ")
          }
        }
      }
      binding.tvCandidateList.text = mCandidatesName
      binding.executePendingBindings()
      itemView.setOnClickListener {
        onItemClicked(election._id)
      }
    }

    private fun getEventStatus(
      currentDate: Date,
      formattedStartingDate: Date?,
      formattedEndingDate: Date?
    ): AppConstants.Status? {
      return when {
        currentDate.before(formattedStartingDate) -> AppConstants.Status.PENDING
        currentDate.after(formattedStartingDate) && currentDate.before(
          formattedEndingDate
        ) -> AppConstants.Status.ACTIVE
        currentDate.after(formattedEndingDate) -> AppConstants.Status.FINISHED
        else -> null
      }
    }

    private fun getEventColor(
      currentDate: Date,
      formattedStartingDate: Date?,
      formattedEndingDate: Date?
    ): Int {
      return when {
        currentDate.before(formattedStartingDate) -> drawable.pending_election_label
        currentDate.after(formattedStartingDate) && currentDate.before(
          formattedEndingDate
        ) -> drawable.active_election_label
        currentDate.after(formattedEndingDate) -> drawable.finished_election_label
        else -> drawable.finished_election_label
      }
    }
  }

  object ElectionItemDiffCallback : DiffUtil.ItemCallback<Election>() {
    override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
      return oldItem._id == newItem._id
    }

    override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
      return oldItem == newItem
    }
  }
}
