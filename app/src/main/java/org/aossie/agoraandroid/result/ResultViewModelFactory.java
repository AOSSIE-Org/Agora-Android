package org.aossie.agoraandroid.result;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import javax.inject.Inject;
import org.aossie.agoraandroid.remote.APIService;

public class ResultViewModelFactory implements ViewModelProvider.Factory {
  private final Application application;
  private final Context context;
  private final APIService apiService;

  public ResultViewModelFactory(Application application, Context context, APIService apiService) {
    this.application = application;
    this.context = context;
    this.apiService= apiService;
  }

  @Override
  public <T extends ViewModel> T create(Class<T> modelClass) {
    return (T) new ResultViewModel(application, context, apiService);
  }
}
