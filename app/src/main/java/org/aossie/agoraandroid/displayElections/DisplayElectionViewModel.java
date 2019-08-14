package org.aossie.agoraandroid.displayElections;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class DisplayElectionViewModel extends AndroidViewModel {
    private final Context context;

    DisplayElectionViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    void getBallot(String token, String id) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> getBallotResponse = apiService.getBallot(token, id);
        getBallotResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Intent intent = new Intent(context, BallotActivity.class);
                    intent.putExtra("ballot_response", response.body());
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });


    }

    void deleteElection(String token, String id) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> deleteElectionResponse = apiService.deleteElection(token, id);
        deleteElectionResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getApplication(), "Election Deleted Successfully", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(getApplication(), HomeActivity.class));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
