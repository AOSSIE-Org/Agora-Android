package org.aossie.agoraandroid.invitevoters;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import net.steamcrafted.loadtoast.LoadToast;

import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class InviteVotersViewModel extends AndroidViewModel {
    private final Context context;
    private LoadToast loadToast;

    public InviteVotersViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    public void inviteVoters(ArrayList<String> mVoterNames, ArrayList<String> mVoterEmails, String id, String token) throws JSONException {
        loadToast = new LoadToast(context);
        loadToast.setText("Inviting Voters");
        loadToast.show();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < mVoterEmails.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", mVoterNames.get(i));
            jsonObject.put("email", mVoterEmails.get(i));
            jsonArray.put(jsonObject);
            Log.d("TAG", "inviteVoters: " + jsonArray);
            sendVoters(id, token, jsonArray);
        }
    }

    private void sendVoters(String id, String token, JSONArray jsonArray) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> sendVotersResponse = apiService.sendVoters(token, id, jsonArray.toString());
        sendVotersResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    loadToast.success();
                    Toast.makeText(context, "Voters Added Successfully", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, HomeActivity.class));
                }
                else if(response.message().equals("Internal Server Error")){
                    loadToast.error();
                    Toast.makeText(context, "Internal server error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
