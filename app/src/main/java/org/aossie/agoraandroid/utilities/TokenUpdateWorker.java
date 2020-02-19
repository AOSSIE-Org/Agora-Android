package org.aossie.agoraandroid.utilities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import androidx.work.impl.constraints.WorkConstraintsCallback;

import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TokenUpdateWorker extends Worker {
    private SharedPrefs sharedPrefs;
    public static final int HOUR_IN_MILLISECONDS = 3600000;
    public static final String TAG = "TokenUpdateWorker";
    public TokenUpdateWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {

        Log.d(TAG,"workercalled");
         sharedPrefs = new SharedPrefs(getApplicationContext());
        String userName = sharedPrefs.getUserName();
        String userPassword = sharedPrefs.getPass();

        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        try {
            String expireOn = sharedPrefs.getTokenExpiresOn();
            if (expireOn != null) {
                Date expiresOn = formatter.parse(expireOn);
                //If the token is expired, get a new one to continue login session of user
                if ((expiresOn.getTime()-currentDate.getTime())<=HOUR_IN_MILLISECONDS){
                    updateToken(userName, userPassword);
                }
                else
                {
                    Log.d(TAG,"tokenexpiry  "+expireOn);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return Result.retry();
        }


        return Result.success();
    }
    private void updateToken(String userName, String userPassword) {

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

                    try {
                        JSONObject jsonObjects = new JSONObject(response.body());

                        JSONObject token = jsonObjects.getJSONObject("token");
                        String expiresOn = token.getString("expiresOn");
                        String key = token.getString("token");
                        Log.d(TAG,"token "+key);
                        Log.d(TAG,"tokenexpiry "+expiresOn);
                        sharedPrefs.saveToken(key);
                        sharedPrefs.saveTokenExpiresOn(expiresOn);

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }



}
