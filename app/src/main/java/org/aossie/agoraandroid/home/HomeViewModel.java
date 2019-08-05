package org.aossie.agoraandroid.home;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.main.MainActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class HomeViewModel extends AndroidViewModel {
    private final Context context;
    private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());


    public HomeViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    void doLogout(String token) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logoutResponse = apiService.logout(token);
        logoutResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Toast.makeText(getApplication(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    sharedPrefs.clearLogin();
                    context.startActivity(new Intent(getApplication(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
