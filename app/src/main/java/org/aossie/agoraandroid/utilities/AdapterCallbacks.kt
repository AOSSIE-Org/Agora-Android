package org.aossie.agoraandroid.utilities

import android.widget.TextView

interface ElectionAdapterCallback{
  fun onItemClicked(electionName: String,
    electionDesc: String,
    startDate: String,
    endDate:String,
    status: String,
    candidate :String,
    id: String)
}

interface ElectionRecyclerAdapterCallback{
  fun onItemClicked(
    _id: String
  )
}

interface CandidateRecyclerAdapterCallback{
  fun onItemClicked(
    name: String,
    itemView: TextView,
    requestCode: Int
  )
}