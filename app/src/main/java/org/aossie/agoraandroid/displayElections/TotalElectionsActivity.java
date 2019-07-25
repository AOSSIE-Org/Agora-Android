package org.aossie.agoraandroid.displayElections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.ElectionsRecyclerAdapter;
import org.aossie.agoraandroid.createElection.ElectionDetailsSharedPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TotalElectionsActivity extends AppCompatActivity {
    RecyclerView rvElectionDetails;
    ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
    ArrayList<String> mElectionNameList = new ArrayList<>();
    ArrayList<String> mElectionDescriptionList = new ArrayList<>();
    ArrayList<String> mElectionStartDateList = new ArrayList<>();
    ArrayList<String> mElectionEndDateList = new ArrayList<>();
    ArrayList<String> mElectionStatusList = new ArrayList<>();
    ArrayList<String> mCandidatesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_elections);
        electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getApplicationContext());
        rvElectionDetails = findViewById(R.id.rv_total_elections);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvElectionDetails.setLayoutManager(mLayoutManager);
        try {
            JSONObject jsonObject = new JSONObject(electionDetailsSharedPrefs.getElectionDetails());
            JSONArray jsonArray = jsonObject.getJSONArray("elections");

            for (int i = 0; i < jsonArray.length(); i++) {
                StringBuilder mCandidateName = new StringBuilder();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                mElectionNameList.add(jsonObject1.getString("name"));
                mElectionDescriptionList.add(jsonObject1.getString("description"));

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date formattedStartingDate = formatter.parse(jsonObject1.getString("start"));
                Date formattedEndingDate = formatter.parse(jsonObject1.getString("end"));
                mElectionStartDateList.add(formattedStartingDate.toString());
                mElectionEndDateList.add(formattedEndingDate.toString());
                Date currentDate = Calendar.getInstance().getTime();
                if (currentDate.before(formattedStartingDate)) {
                    mElectionStatusList.add("Pending");
                } else if (currentDate.after(formattedStartingDate) && currentDate.before(formattedEndingDate)) {
                    mElectionStatusList.add("Active");
                } else if (currentDate.after(formattedEndingDate)) {
                    mElectionStatusList.add("Finished");
                }
                JSONArray jsonArray1 = jsonObject1.getJSONArray("candidates");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    mCandidateName.append(jsonArray1.getString(j)).append("\n");
                }
                mCandidatesList.add(mCandidateName.toString().trim());


            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ElectionsRecyclerAdapter electionsRecyclerAdapter = new ElectionsRecyclerAdapter(mElectionNameList, mElectionDescriptionList, mElectionStartDateList, mElectionEndDateList, mElectionStatusList, mCandidatesList);
        rvElectionDetails.setAdapter(electionsRecyclerAdapter);


    }

}
