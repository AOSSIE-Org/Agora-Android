package org.aossie.agoraandroid.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.facebook.shimmer.ShimmerFrameLayout;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.createelection.CreateElectionOne;
import org.aossie.agoraandroid.createelection.ElectionDetailsSharedPrefs;
import org.aossie.agoraandroid.displayelections.ActiveElectionsActivity;
import org.aossie.agoraandroid.displayelections.FinishedElectionsActivity;
import org.aossie.agoraandroid.displayelections.PendingElectionsActivity;
import org.aossie.agoraandroid.displayelections.TotalElectionsActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
  private TextView mActiveCountTextView, mPendingCountTextView, mTotalCountTextView,
      mFinishedCountTextView;
  private ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
  private int mActiveCount = 0, mFinishedCount = 0, mPendingCount = 0, flag = 0;
  private ShimmerFrameLayout mShimmerViewContainer;
  private ConstraintLayout constraintLayout;
  private SharedPrefs sharedPrefs;
  private SwipeRefreshLayout mSwipeRefreshLayout;

  public HomeFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getActivity());
    sharedPrefs = new SharedPrefs(getActivity());
    View view = inflater.inflate(R.layout.fragment_home, null);

    mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
    constraintLayout = view.findViewById(R.id.constraintLayout);

    mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
    mSwipeRefreshLayout.setColorSchemeResources(R.color.logo_yellow, R.color.logo_green);
    CardView mTotalElectionsCardView = view.findViewById(R.id.card_view_total_elections);
    CardView mPendingElectionsCardView = view.findViewById(R.id.card_view_pending_elections);
    CardView mActiveElectionsCardView = view.findViewById(R.id.card_view_active_elections);
    CardView mFinishedElectionsCardView = view.findViewById(R.id.card_view_finished_elections);

    mActiveCountTextView = view.findViewById(R.id.text_view_active_count);
    mPendingCountTextView = view.findViewById(R.id.text_view_pending_count);
    mTotalCountTextView = view.findViewById(R.id.text_view_total_count);
    mFinishedCountTextView = view.findViewById(R.id.text_view_finished_count);

    Button createElection = view.findViewById(R.id.button_create_election);
    mActiveElectionsCardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), ActiveElectionsActivity.class));
      }
    });
    mPendingElectionsCardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), PendingElectionsActivity.class));
      }
    });
    mFinishedElectionsCardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), FinishedElectionsActivity.class));
      }
    });
    mTotalElectionsCardView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), TotalElectionsActivity.class));
      }
    });
    createElection.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(getActivity(), CreateElectionOne.class));
      }
    });
    mSwipeRefreshLayout.setOnRefreshListener(
        new SwipeRefreshLayout.OnRefreshListener() {
          @Override
          public void onRefresh() {
            doYourUpdate();
          }
        }
    );

    String userName = sharedPrefs.getUserName();
    String userPassword = sharedPrefs.getPass();
    SimpleDateFormat formatter =
        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
    Date currentDate = Calendar.getInstance().getTime();
    try {
      String expireOn = sharedPrefs.getTokenExpiresOn();
      if (expireOn != null) {
        Date expiresOn = formatter.parse(expireOn);
        //If the token is expired, get a new one to continue login session of user
        if (currentDate.after(expiresOn)) {
          updateToken(userName, userPassword);
        }
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    getElectionData(sharedPrefs.getToken());
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle
      savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
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
          mShimmerViewContainer.stopShimmer();
          mShimmerViewContainer.setVisibility(View.GONE);
          constraintLayout.setVisibility(View.VISIBLE);
          try {
            JSONObject jsonObjects = new JSONObject(response.body());

            JSONObject token = jsonObjects.getJSONObject("token");
            String expiresOn = token.getString("expiresOn");
            String key = token.getString("token");

            sharedPrefs.saveToken(key);
            sharedPrefs.saveTokenExpiresOn(expiresOn);
          } catch (JSONException e) {
            e.printStackTrace();
          }
        } else {
          Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_SHORT)
              .show();
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        Toast.makeText(getContext(), "Something went wrong please try again", Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  private void getElectionData(String token) {
    APIService apiService = RetrofitClient.getAPIService();
    Call<String> electionDataResponse = apiService.getAllElections(token);
    electionDataResponse.enqueue(new Callback<String>() {
      @Override
      public void onResponse(Call<String> call, Response<String> response) {
        if (response.message().equals("OK")) {

          mShimmerViewContainer.stopShimmer();
          mShimmerViewContainer.setVisibility(View.GONE);
          constraintLayout.setVisibility(View.VISIBLE);

          electionDetailsSharedPrefs.saveElectionDetails(response.body());
          try {
            JSONObject jsonObject = new JSONObject(response.body());
            JSONArray jsonArray = jsonObject.getJSONArray("elections");

            for (int i = 0; i < jsonArray.length(); i++) {
              JSONObject jsonObject1 = jsonArray.getJSONObject(i);
              String startingDate = jsonObject1.getString("start");
              String endingDate = jsonObject1.getString("end");
              SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
              Date formattedStartingDate = formatter.parse(startingDate);
              Date formattedEndingDate = formatter.parse(endingDate);
              Date currentDate = Calendar.getInstance().getTime();

              // Separating into Active, Finished or Pending Elections
              if (flag == 0) {

                if (currentDate.before(formattedStartingDate)) {
                  mPendingCount++;
                } else if (currentDate.after(formattedStartingDate) && currentDate.before(
                    formattedEndingDate)) {
                  mActiveCount++;
                } else if (currentDate.after(formattedEndingDate)) {
                  mFinishedCount++;
                }
              }
            }
            flag++;

            mTotalCountTextView.setText(String.valueOf(jsonArray.length()));
            mPendingCountTextView.setText(String.valueOf(mPendingCount));
            mActiveCountTextView.setText(String.valueOf(mActiveCount));
            mFinishedCountTextView.setText(String.valueOf(mFinishedCount));
            mSwipeRefreshLayout.setRefreshing(false); // Disables the refresh icon
          } catch (JSONException e) {
            e.printStackTrace();
          } catch (ParseException e) {
            e.printStackTrace();
          }
        }
      }

      @Override
      public void onFailure(Call<String> call, Throwable t) {
        mShimmerViewContainer.stopShimmer();
        mShimmerViewContainer.setVisibility(View.GONE);
        constraintLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false); // Disables the refresh icon
        Toast.makeText(getActivity(), "Something went wrong please refresh", Toast.LENGTH_SHORT)
            .show();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    mShimmerViewContainer.startShimmer();
  }

  @Override
  public void onPause() {
    mShimmerViewContainer.stopShimmer();
    super.onPause();
  }

  private void doYourUpdate() {
    constraintLayout.setVisibility(View.GONE);
    mShimmerViewContainer.setVisibility(View.VISIBLE);
    mShimmerViewContainer.startShimmer();
    getElectionData(sharedPrefs.getToken());//try to fetch data again
  }
}
