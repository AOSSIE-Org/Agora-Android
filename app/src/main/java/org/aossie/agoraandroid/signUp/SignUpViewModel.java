package org.aossie.agoraandroid.signUp;


import android.app.Application;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpViewModel extends AndroidViewModel {

    public SignUpViewModel(@NonNull Application application) {
        super(application);
    }


    public void signUpRequest(final String userName, String userPassword, String userEmail, String firstName, String lastName) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", userName);
            jsonObject.put("password", userPassword);
            jsonObject.put("email", userEmail);
            jsonObject.put("firstName", firstName);
            jsonObject.put("lastName", lastName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<String> signUpResponse = apiService.createUser(jsonObject.toString());
        signUpResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.message().equals("timeout")){
                    Toast.makeText(getApplication(), "Network Connection issues please try again", Toast.LENGTH_LONG).show();
                }
                if (response.code()== 200) {
                    Toast.makeText(getApplication(), "An activation link has been sent to your email. Follow it to activate your account.", Toast.LENGTH_LONG).show();
                } else if (response.code() == 409) {
                    Toast.makeText(getApplication(), "User With Same UserName already Exists", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_LONG).show();
                }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
