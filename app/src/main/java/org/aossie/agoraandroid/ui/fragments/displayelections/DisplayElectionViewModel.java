package org.aossie.agoraandroid.ui.fragments.displayelections;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import net.steamcrafted.loadtoast.LoadToast;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.ui.fragments.auth.AuthListener;
import org.aossie.agoraandroid.ui.fragments.moreOptions.HomeActivity;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class DisplayElectionViewModel extends AndroidViewModel {
  private final Context context;
  private LoadToast loadToast;
  private DisplayElectionListener displayElectionListener;

  DisplayElectionViewModel(@NonNull Application application, Context context) {
    super(application);
    this.context = context;
  }

  void getBallot(String token, String id) {
    loadToast = new LoadToast(context);
    loadToast.setText("Getting Details");
    loadToast.show();
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> getBallotResponse = apiService.getBallot(token, id);
    getBallotResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.message().equals("OK")) {
          loadToast.success();
          Intent intent = new Intent(context, BallotActivity.class);
          intent.putExtra("ballot_response", response.body());
          context.startActivity(intent);
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        loadToast.error();
        Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  void getVoter(String token, String id) {
    loadToast = new LoadToast(context);
    loadToast.setText("Getting Voters");
    loadToast.show();
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> getVotersResponse = apiService.getVoters(token, id);
    getVotersResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
        if (response.message().equals("OK")) {
          loadToast.success();
          if (response.body() != null) {
            displayElectionListener.onGetVotersSuccess(response.body());
          }
          //Intent intent = new Intent(context, VotersActivity.class);
          //intent.putExtra("voters_response", response.body());
          //context.startActivity(intent);
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        loadToast.error();
        Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT).show();
      }
    });
  }

  void deleteElection(String token, String id) {
    loadToast = new LoadToast(context);
    loadToast.setText("Deleting Election");
    loadToast.show();
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> deleteElectionResponse = apiService.deleteElection(token, id);
    deleteElectionResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.message().equals("OK")) {
          loadToast.success();
          Toast.makeText(getApplication(), "Election Deleted Successfully",
              Toast.LENGTH_SHORT)
              .show();
          context.startActivity(new Intent(getApplication(), HomeActivity.class));
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        loadToast.show();
        Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT).show();
      }
    });
  }
}
