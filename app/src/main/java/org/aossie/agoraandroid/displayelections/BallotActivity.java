package org.aossie.agoraandroid.displayelections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.BallotRecyclerAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BallotActivity extends AppCompatActivity {
    private String ballotResponse;
    private final ArrayList<String> mVoterEmailList = new ArrayList<>();
    private final ArrayList<String> mVoteBallotList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ballot);
        RecyclerView rvBallotDetails = findViewById(R.id.recycler_view_ballots);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvBallotDetails.setLayoutManager(mLayoutManager);

        if (getIntent().hasExtra("ballot_response")) {
            ballotResponse = getIntent().getStringExtra("ballot_response");
        }
        try {
            JSONObject jsonObject = new JSONObject(ballotResponse);
            JSONArray ballotJsonArray = jsonObject.getJSONArray("ballots");
            for (int i = 0; i < ballotJsonArray.length(); i++) {
                JSONObject ballotJsonObject = ballotJsonArray.getJSONObject(i);

                mVoterEmailList.add(ballotJsonObject.getString("voterEmail"));
                mVoteBallotList.add(ballotJsonObject.getString("voteBallot"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BallotRecyclerAdapter ballotRecyclerAdapter = new BallotRecyclerAdapter(mVoterEmailList, mVoteBallotList);
        rvBallotDetails.setAdapter(ballotRecyclerAdapter);
    }
}
