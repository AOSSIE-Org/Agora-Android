package org.aossie.agoraandroid.forgotpassword;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.AppConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends AndroidViewModel {
  private Context context;
  private MutableLiveData<Boolean> mutableIsValidUsername  = new MutableLiveData<>();
  public LiveData<Boolean> isValidUsername = mutableIsValidUsername;
  private MutableLiveData<String> mutableError = new MutableLiveData<>();
  public LiveData<String> error = mutableError;

  ForgotPasswordViewModel(@NonNull Application application, Context context) {
    super(application);
    this.context = context;
  }

  void sendForgotPassLink(String userName) {

    APIService apiService = RetrofitClient.getAPIService();
    Call<String> forgotPasswordResponse = apiService.sendForgotPassword(userName);
    forgotPasswordResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.message().equals(AppConstants.ok)) {
          mutableIsValidUsername.setValue(true);
        } else {
          mutableError.setValue(getApplication().getString(R.string.invalid_username));
          mutableIsValidUsername.setValue(false);
        }
      }
      @Override
      public void onFailure(Call<String> call, Throwable t) {
        mutableError.setValue(getApplication().getString(R.string.something_went_wrong_please_try_again_later));
      }
    });
  }
}
