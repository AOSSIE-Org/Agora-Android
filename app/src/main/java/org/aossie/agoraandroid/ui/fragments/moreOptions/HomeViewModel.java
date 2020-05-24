package org.aossie.agoraandroid.ui.fragments.moreOptions;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import net.steamcrafted.loadtoast.LoadToast;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
  private final Context context;
  private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());
  private LoadToast loadToast;
  public AuthListener authListener;

  HomeViewModel(@NonNull Application application, Context context) {
    super(application);
    this.context = context;
  }

  void doLogout(String token) {
    loadToast = new LoadToast(context);
    loadToast.setText("Logging out");
    loadToast.show();
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> logoutResponse = apiService.logout(token);
    logoutResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
        if (response.message().equals("OK")) {
          loadToast.success();
          Toast.makeText(getApplication(), "Logged Out Successfully", Toast.LENGTH_SHORT)
              .show();
          sharedPrefs.clearLogin();
          authListener.onSuccess();
        }
      }

      @Override
      public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
        loadToast.error();
        Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT).show();
      }
    });
  }
}
