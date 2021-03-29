package org.aossie.agoraandroid.result;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import javax.inject.Inject;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.ui.fragments.electionDetails.ResultFetchFailureListener;

public class ResultViewModelFactory implements ViewModelProvider.Factory {
  private final Application application;
  private final Context context;
  private final APIService apiService;
  private ResultFetchFailureListener electionResultListener;

  public ResultViewModelFactory(Application application, Context context, APIService apiService, ResultFetchFailureListener electionResultListener) {
    this.application = application;
    this.context = context;
    this.apiService= apiService;
    this.electionResultListener = electionResultListener;
  }

  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    return (T) new ResultViewModel(application, context, apiService, electionResultListener);
  }
}
