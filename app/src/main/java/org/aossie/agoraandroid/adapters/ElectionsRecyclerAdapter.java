package org.aossie.agoraandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.aossie.agoraandroid.R;


import java.util.ArrayList;

public class ElectionsRecyclerAdapter extends RecyclerView.Adapter<ElectionsRecyclerAdapter.ElectionsViewHolder> {

    private final ArrayList<String> electionNameList,electionDescriptionList, startDateList, endDateList, statusList, candidateList;

    public ElectionsRecyclerAdapter(ArrayList<String> electionNameList,ArrayList<String> electionDescriptionList, ArrayList<String> startDateList, ArrayList<String> endDateList, ArrayList<String> statusList,ArrayList<String> candidateList) {
        this.electionNameList = electionNameList;
        this.electionDescriptionList=electionDescriptionList;
        this.startDateList = startDateList;
        this.endDateList = endDateList;
        this.statusList = statusList;
        this.candidateList = candidateList;


    }

    @NonNull
    @Override
    public ElectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = layoutInflater.inflate(R.layout.list_item_election_details, parent, false);

        return new ElectionsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ElectionsViewHolder holder, int position) {
        holder.mElectionName.setText(electionNameList.get(position));
        holder.mElectionDescription.setText(electionDescriptionList.get(position));
        holder.mEndDate.setText(endDateList.get(position));
        holder.mStartDate.setText(startDateList.get(position));
        holder.mStatus.setText(statusList.get(position));
        holder.mCandidateList.setText(candidateList.get(position));


    }

    @Override
    public int getItemCount() {
        return electionNameList.size();
    }

    class ElectionsViewHolder extends RecyclerView.ViewHolder {
        final TextView mElectionName, mElectionDescription, mStartDate, mEndDate,mStatus,mCandidateList;

        public ElectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            mElectionName = itemView.findViewById(R.id.text_view_election_name);
            mElectionDescription = itemView.findViewById(R.id.text_view_election_description);
            mStartDate=itemView.findViewById(R.id.text_view_start_date);
            mEndDate=itemView.findViewById(R.id.text_view_end_date);
            mStatus=itemView.findViewById(R.id.text_view_status);
            mCandidateList=itemView.findViewById(R.id.text_view_candidate_list);
        }
    }
}
