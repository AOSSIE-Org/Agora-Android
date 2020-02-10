package org.aossie.agoraandroid.forgotpassword;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import net.steamcrafted.loadtoast.LoadToast;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends AndroidViewModel {
    private LoadToast loadToast;
    private Context context;

    ForgotPasswordViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    void sendForgotPassLink(String userName) {
        loadToast = new LoadToast(context);
        loadToast.setText(getApplication().getResources().getString(R.string.processing_text));
        loadToast.show();

        APIService apiService = RetrofitClient.getAPIService();
        Call<String> forgotPasswordResponse = apiService.sendForgotPassword(userName);
        forgotPasswordResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loadToast.success();
                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.link_sent_text), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.try_again_text), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
