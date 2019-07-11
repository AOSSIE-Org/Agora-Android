package org.aossie.agoraandroid.createElection;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import org.aossie.agoraandroid.utilities.SharedPrefs2;
import org.aossie.agoraandroid.utilities.TinyDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CreateElectionViewModel extends AndroidViewModel {
    private SharedPrefs2 sharedPrefs2 = new SharedPrefs2(getApplication());
    private TinyDB tinydb = new TinyDB(getApplication());


    public CreateElectionViewModel(@NonNull Application application, Context context) {
        super(application);
        Context context1 = context;

    }

    public void createElection() {
        ArrayList<String> candidates = tinydb.getListString("Candidates");
        JSONArray jsArray = new JSONArray(candidates);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", sharedPrefs2.getElectionName());
            jsonObject.put("description", sharedPrefs2.getElectionDesc());
            jsonObject.put("voterListVisibility", sharedPrefs2.getVoterListVisibility());
            jsonObject.put("startingDate", sharedPrefs2.getStartTime());
            jsonObject.put("endingDate", sharedPrefs2.getEndTime());
            jsonObject.put("isInvite", sharedPrefs2.getIsInvite());
            jsonObject.put("isRealTime", sharedPrefs2.getIsRealTime());
            jsonObject.put("votingAlgo", sharedPrefs2.getVotingAlgo());
            jsonObject.put("candidates", jsArray);
            jsonObject.put("noVacancies", 0);
            Log.d("TAG", "createElection: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


}
