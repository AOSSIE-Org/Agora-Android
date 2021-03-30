package org.aossie.agoraandroid.result;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.ui.fragments.electionDetails.ResultFetchFailureListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultViewModel extends AndroidViewModel {
  private final Context context;
  private ResultFetchFailureListener electionResultListener;

  private APIService apiService;

  public ResultViewModel(@NonNull Application application, Context context, APIService apiService, ResultFetchFailureListener failureListener) {
    super(application);
    this.context = context;
    this.apiService = apiService;
    this.electionResultListener = failureListener;
  }

  public void getResult(String token, String id) {
    Call<String> getResultResponse = apiService.getResult(token, id);
    getResultResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.message().equals("OK")) {
          try {
            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            JSONObject candidateJsonObject = jsonObject.getJSONObject("candidate");
            JSONObject scoreJsonObject = jsonObject.getJSONObject("score");

            String candidateName = candidateJsonObject.getString("name");
            String numerator = scoreJsonObject.getString("numerator");
            String denominator = scoreJsonObject.getString("denominator");

            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra("name", candidateName);
            intent.putExtra("numerator", numerator);
            intent.putExtra("denominator", denominator);
            context.startActivity(intent);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        } else if (response.message().equals("No Content")) {
          electionResultListener.onResultFetchMessage(R.string.nothing_to_show_here);
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        electionResultListener.onResultFetchMessage(R.string.something_went_wrong_please_try_again_later);
      }
    });
  }
}
