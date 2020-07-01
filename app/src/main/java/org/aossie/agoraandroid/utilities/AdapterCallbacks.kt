package org.aossie.agoraandroid.utilities

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