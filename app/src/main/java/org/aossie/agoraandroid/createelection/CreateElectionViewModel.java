package org.aossie.agoraandroid.createelection;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import net.steamcrafted.loadtoast.LoadToast;
import org.aossie.agoraandroid.home.HomeActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.aossie.agoraandroid.utilities.TinyDB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class CreateElectionViewModel extends AndroidViewModel {
  private final Context context;
  private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());
  private LoadToast loadToast;

  CreateElectionViewModel(@NonNull Application application, Context context) {
    super(application);
    this.context = context;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public void createElection(
      String mElectionName,
      String mElectionDescription,
      String mStartDate,
      String mEndDate,
      ArrayList<String> mCandidateList,
      String mSelectedVotingAlgorithm,
      Boolean mFinalIsInvite,
      Boolean mFinalIsRealTime,
      Boolean mVoterListVisibility,
      String mBallot
  ) {
    loadToast = new LoadToast(context);
    loadToast.setText("Creating Election");
    loadToast.show();
    JSONArray jsArray = new JSONArray(mCandidateList);
    String token = sharedPrefs.getToken();
    final JSONObject jsonObject = new JSONObject();
    try {
      Map<String, String> map = new HashMap<>();
      map.put("voteBallot", "");
      map.put("hash", "");
      JSONArray ballot = new JSONArray(new Map[] { map });
      jsonObject.put("ballot", ballot);  //Append the other JSONObject to the parent one
      jsonObject.put("name", mElectionName);
      jsonObject.put("description", mElectionDescription);
      jsonObject.put("voterListVisibility", mVoterListVisibility);
      jsonObject.put("startingDate", mStartDate);
      jsonObject.put("endingDate", mEndDate);
      jsonObject.put("isInvite", mFinalIsInvite);
      jsonObject.put("ballotVisibility", mBallot);
      jsonObject.put("isRealTime", mFinalIsRealTime);
      jsonObject.put("votingAlgo", mSelectedVotingAlgorithm);
      jsonObject.put("candidates", jsArray);
      jsonObject.put("noVacancies", 1);
      jsonObject.put("electionType", "Election");
    } catch (JSONException e) {
      e.printStackTrace();
    }
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> electionResponse = apiService.createElection(jsonObject.toString(), token);
    electionResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {

        if (response.message().equals("OK")) {
          loadToast.success();
          Toast.makeText(getApplication(), "Created Successfully", Toast.LENGTH_SHORT).show();
          Intent intent = new Intent(context, HomeActivity.class);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
          context.startActivity(intent);
        } else {
          loadToast.error();
          Toast.makeText(getApplication(), "Something went wrong please try again",
              Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        loadToast.error();
        Toast.makeText(getApplication(), "Something went wrong please try again",
            Toast.LENGTH_SHORT).show();
      }
    });
  }
}