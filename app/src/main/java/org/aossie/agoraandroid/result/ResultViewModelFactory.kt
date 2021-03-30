package org.aossie.agoraandroid.result

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.Factory
import org.aossie.agoraandroid.remote.APIService
import org.aossie.agoraandroid.ui.fragments.electionDetails.ResultFetchFailureListener

class ResultViewModelFactory(
  private val application: Application,
  private val context: Context,
  private val apiService: APIService,
  private val electionResultListener: ResultFetchFailureListener
) :
    Factory {
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return ResultViewModel(application, context, apiService, electionResultListener) as T
  }
}