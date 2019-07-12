package org.aossie.agoraandroid.createElection;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.aossie.agoraandroid.utilities.TinyDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class CreateElectionViewModel extends AndroidViewModel {
    private Context context;
    private ElectionDetails electionDetails = new ElectionDetails(getApplication());
    private SharedPrefs sharedPrefs = new SharedPrefs(getApplication());
    private TinyDB tinydb = new TinyDB(getApplication());


    CreateElectionViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createElection() {
        ArrayList<String> candidates = tinydb.getListString("Candidates");
        JSONArray jsArray = new JSONArray(candidates);
        String token = sharedPrefs.getToken();
        final JSONObject jsonObject = new JSONObject();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("voteBallot", "");
            map.put("voterEmail", "");
            JSONArray ballot = new JSONArray(new Map[]{map});
            jsonObject.put("ballot", ballot);  //Append the other JSONObject to the parent one
            jsonObject.put("name", electionDetails.getElectionName());
            jsonObject.put("description", electionDetails.getElectionDesc());
            jsonObject.put("voterListVisibility", electionDetails.getVoterListVisibility());
            jsonObject.put("startingDate", electionDetails.getStartTime());
            jsonObject.put("endingDate", electionDetails.getEndTime());
            jsonObject.put("isInvite", electionDetails.getIsInvite());
            jsonObject.put("ballotVisibility", electionDetails.getBallotVisibility());
            jsonObject.put("isRealTime", electionDetails.getIsRealTime());
            jsonObject.put("votingAlgo", electionDetails.getVotingAlgo());
            jsonObject.put("candidates", jsArray);
            jsonObject.put("noVacancies", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> electionResponse = apiService.createElection(jsonObject.toString(), token);
        electionResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.message().equals("OK")) {
                    Toast.makeText(getApplication(), "Created Successfully", Toast.LENGTH_SHORT).show();
                    electionDetails.clearElectionData();
                    context.startActivity(new Intent(context, HomeActivity.class));
                } else {
                    Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}