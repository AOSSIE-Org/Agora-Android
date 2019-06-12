package org.aossie.agoraandroid.main.signup;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


import org.aossie.agoraandroid.main.Remote.APIService;
import org.aossie.agoraandroid.main.Remote.RetrofitClient;
import org.aossie.agoraandroid.main.login.LogInActivity;

import java.util.Observable;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends Observable {

    private Context context;
    public SignUpViewModel(Context context) {
        this.context=context;
    }


    public void signUpRequest(){

        APIService apiService= RetrofitClient.getAPIService();
        Call<ResponseBody> signUpResponse=apiService.createUser("mukul","mukul","mukul@gmail.com","mukul","kumar");
        signUpResponse.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context,""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


}
