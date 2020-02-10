package org.aossie.agoraandroid.signup;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import net.steamcrafted.loadtoast.LoadToast;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.login.LoginActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class SignUpViewModel extends AndroidViewModel {
    private final Context context;
    private LoadToast loadToast;

    public SignUpViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }


    public void signUpRequest(final String userName, String userPassword, String userEmail, String firstName, String lastName, String securityQuestion, String securityAnswer) {
        JSONObject jsonObject = new JSONObject();
        JSONObject securityJsonObject = new JSONObject();
        loadToast = new LoadToast(context);
        loadToast.setText("Signing in");
        loadToast.show();
        try {

            jsonObject.put("identifier", userName);
            jsonObject.put("password", userPassword);
            jsonObject.put("email", userEmail);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);
            securityJsonObject.put("question", securityQuestion);
            securityJsonObject.put("answer", securityAnswer);
            jsonObject.put("securityQuestion", securityJsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<String> signUpResponse = apiService.createUser(jsonObject.toString());
        signUpResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("timeout")) {
                    loadToast.error();
                    Toast.makeText(getApplication(),getApplication().getResources().getString(R.string.network_error), Toast.LENGTH_LONG).show();
                }
                if (response.code() == 200) {
                    loadToast.success();
                    Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.activation_link_send), Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                } else if (response.code() == 409) {
                    loadToast.error();
                    Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.same_username_error), Toast.LENGTH_LONG).show();
                } else {
                    loadToast.error();
                    Toast.makeText(getApplication(), getApplication().getResources().getString(R.string.try_again_text), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
