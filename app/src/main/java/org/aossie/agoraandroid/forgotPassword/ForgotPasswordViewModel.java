package org.aossie.agoraandroid.forgotPassword;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends AndroidViewModel {

    ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    void sendForgotPassLink(String userName) {

        APIService apiService = RetrofitClient.getAPIService();
        Call<String> forgotPasswordResponse = apiService.sendForgotPassword(userName);
        forgotPasswordResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getApplication(), "Link Sent, Please Check Your Emails", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
