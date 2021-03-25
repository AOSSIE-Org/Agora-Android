package org.aossie.agoraandroid.result;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.google.android.material.snackbar.Snackbar;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.AppConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultViewModel extends AndroidViewModel {
  private final Context context;

  public ResultViewModel(@NonNull Application application, Context context) {
    super(application);
    this.context = context;
  }

  public void getResult(String token, String id, final View rootView) {
    APIService apiService = RetrofitClient.getAPIService();
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
          final Snackbar s = Snackbar.make(rootView, "Nothing to show here", Snackbar.LENGTH_INDEFINITE);
          s.setAction(AppConstants.ok, new View.OnClickListener() {
            @Override public void onClick(View view) {
              s.dismiss();
            }
          });
          s.show();
          //Toast.makeText(getApplication(), "Nothing to show here", Toast.LENGTH_SHORT).show();
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later",
            Toast.LENGTH_SHORT).show();
      }
    });
  }
}
