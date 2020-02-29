package org.aossie.agoraandroid.createelection

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*


class CreateElectionTwoViewModel(candidateArrayList: ArrayList<String>) : ViewModel() {
    // TODO: Implement the ViewModel
    var input: String? = null
    private var CandidatesLiveData: MutableLiveData<ArrayList<String>>? = null
    private var CandidateArrayList: ArrayList<String>? = null

    init {
        CandidatesLiveData = MutableLiveData<ArrayList<String>>()
        initialize(candidateArrayList)
    }

    fun getAllCandidatesLive(): MutableLiveData<ArrayList<String>>? {
        return CandidatesLiveData
    }

    fun getAllCandidates(): ArrayList<String>? {
        return CandidateArrayList
    }

    fun getEntered(): String? {
        return input
    }

    fun addCandidate(cName: String) {
        CandidateArrayList!!.add(cName)

    }

    fun removeCandidate(pos: Int) {
        CandidateArrayList!!.removeAt(pos)
    }

    fun initialize(candidateList: ArrayList<String>) {
        CandidateArrayList = candidateList
        //CandidatesLiveData!!.setValue(CandidateArrayList)
    }
}
