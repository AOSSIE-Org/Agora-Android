package org.aossie.agoraandroid.home;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.createElection.CreateElectionOne;
import org.aossie.agoraandroid.createElection.ElectionDetailsSharedPrefs;
import org.aossie.agoraandroid.displayElections.ActiveElectionsActivity;
import org.aossie.agoraandroid.displayElections.FinishedElectionsActivity;
import org.aossie.agoraandroid.displayElections.PendingElectionsActivity;
import org.aossie.agoraandroid.displayElections.TotalElectionsActivity;
import org.aossie.agoraandroid.remote.APIService;
import org.aossie.agoraandroid.remote.RetrofitClient;
import org.aossie.agoraandroid.utilities.SharedPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private TextView mActiveCountTextView, mPendingCountTextView, mTotalCountTextView, mFinishedCountTextView;
    private ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
    private int mActiveCount = 0, mFinishedCount = 0, mPendingCount = 0, flag = 0;
    private ShimmerFrameLayout mShimmerViewContainer;
    private ConstraintLayout constraintLayout;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getActivity());
        SharedPrefs sharedPrefs = new SharedPrefs(getActivity());
        View view = inflater.inflate(R.layout.fragment_home, null);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        constraintLayout = view.findViewById(R.id.constraintLayout);


        CardView mTotalElectionsCardView = view.findViewById(R.id.card_view_total_elections);
        CardView mPendingElectionsCardView = view.findViewById(R.id.card_view_pending_elections);
        CardView mActiveElectionsCardView = view.findViewById(R.id.card_view_active_elections);
        CardView mFinishedElectionsCardView = view.findViewById(R.id.card_view_finished_elections);

        mActiveCountTextView = view.findViewById(R.id.text_view_active_count);
        mPendingCountTextView = view.findViewById(R.id.text_view_pending_count);
        mTotalCountTextView = view.findViewById(R.id.text_view_total_count);
        mFinishedCountTextView = view.findViewById(R.id.text_view_finished_count);

        Button createElection = view.findViewById(R.id.button_create_election);
        getElectionData(sharedPrefs.getToken());
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                            Log.d("TAG", "onResponse: " + currentDate);

                            // Separating into Active, Finished or Pending Elections
                            if (flag == 0) {

                                if (currentDate.before(formattedStartingDate)) {
                                    mPendingCount++;
                                    Log.d("TAG", "onResponse: " + mPendingCount);
                                } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
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
}
