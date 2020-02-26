package org.aossie.agoraandroid.createelection

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CreateElectionOneViewModelFactory(private val context:Context): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CreateElectionOneViewModel(context)as T
    }

}