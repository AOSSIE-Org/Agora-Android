package org.aossie.agoraandroid.utilities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.aossie.agoraandroid.createelection.CreateElectionTwoViewModel

class CandidatesListFactory(private val mExtra: ArrayList<String>) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateElectionTwoViewModel(mExtra) as T
    }
}