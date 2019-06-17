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
//        JsonObject jsonObject=new JsonObject();
       try {
//            jsonObject.addProperty("identifier", userName);
//            jsonObject.addProperty("password", userPassword);
//            jsonObject.addProperty("email", userEmail);
//            jsonObject.addProperty("firstName",firstName);
//            jsonObject.addProperty("lastName", lastName);

            jsonObject.put("identifier", userName);
            jsonObject.put("password", userPassword);
            jsonObject.put("email", userEmail);
            jsonObject.put("firstName",firstName);
            jsonObject.put("lastName", lastName);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<JSONObject> signUpResponse = apiService.createUser(jsonObject);
        signUpResponse.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                Toast.makeText(getApplication(), "" + response.toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplication(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }




}
