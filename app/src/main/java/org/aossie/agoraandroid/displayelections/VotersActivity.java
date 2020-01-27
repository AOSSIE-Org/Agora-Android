package org.aossie.agoraandroid.displayelections;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.VotersRecyclerAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VotersActivity extends AppCompatActivity {
    private String voterResponse;
    private final ArrayList<String> mVoterEmailList = new ArrayList<>();
    private final ArrayList<String> mVoterNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters);
        RecyclerView rvVotersDetails = findViewById(R.id.recycler_view_voters);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvVotersDetails.setLayoutManager(mLayoutManager);

        Toolbar toolbar = findViewById(R.id.toolbar_voter_list);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
        }

        if (getIntent().hasExtra("voters_response")) {
            voterResponse = getIntent().getStringExtra("voters_response");
        }
        try {
            JSONObject jsonObject = new JSONObject(voterResponse);
            JSONArray ballotJsonArray = jsonObject.getJSONArray("voters");
            for (int i = 0; i < ballotJsonArray.length(); i++) {
                JSONObject ballotJsonObject = ballotJsonArray.getJSONObject(i);

                mVoterEmailList.add(ballotJsonObject.getString("email"));
                mVoterNameList.add(ballotJsonObject.getString("name"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        VotersRecyclerAdapter votersRecyclerAdapter = new VotersRecyclerAdapter(mVoterNameList, mVoterEmailList);
        rvVotersDetails.setAdapter(votersRecyclerAdapter);
    }
}
