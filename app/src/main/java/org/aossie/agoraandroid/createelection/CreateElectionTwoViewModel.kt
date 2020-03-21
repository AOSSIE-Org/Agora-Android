package org.aossie.agoraandroid.createelection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class CreateElectionTwoViewModel(candidateArrayList: ArrayList<String>) : ViewModel() {
  var input: String? = null
  private var CandidatesLiveData: MutableLiveData<ArrayList<String>>? = null
  private var CandidateArrayList: ArrayList<String>? = null

  init {
    initialize(candidateArrayList)
  }

  fun getAllCandidates(): ArrayList<String>? {
    return CandidateArrayList
  }

  fun addCandidate(cName: String) {
    CandidateArrayList!!.add(cName)

  }

  fun removeCandidate(pos: Int) {
    CandidateArrayList!!.removeAt(pos)
  }

  fun initialize(candidateList: ArrayList<String>) {
    CandidateArrayList = candidateList
  }
}
