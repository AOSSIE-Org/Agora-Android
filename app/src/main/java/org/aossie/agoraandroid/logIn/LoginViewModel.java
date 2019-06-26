package org.aossie.agoraandroid.logIn;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.gson.Gson;

import org.aossie.agoraandroid.SharedPrefs;
import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private final Context context;
    private SharedPrefs sharedPrefs = new SharedPrefs(getApplication());


    public LoginViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    public void logInRequest(final String userName, String userPassword) {
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", userName);
            jsonObject.put("password", userPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logInResponse = apiService.logIn(jsonObject.toString());
        logInResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    String data = response.body();

                    Gson g = new Gson();
                    User user = g.fromJson(data, User.class);

                    String username = user.getUsername();
                    String email = user.getEmail();
                    String firstName = user.getFirstName();
                    String lastName = user.getLastName();

                    sharedPrefs.saveUserName(username);
                    sharedPrefs.saveEmail(email);
                    sharedPrefs.saveFullName(firstName, lastName);

                    context.startActivity(new Intent(context, HomeActivity.class));
                } else {
                    Toast.makeText(getApplication(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
