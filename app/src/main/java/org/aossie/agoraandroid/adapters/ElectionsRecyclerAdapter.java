package org.aossie.agoraandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.displayelections.ElectionActivity;

public class ElectionsRecyclerAdapter
    extends RecyclerView.Adapter<ElectionsRecyclerAdapter.ElectionsViewHolder> {

  private final ArrayList<String> electionNameList, electionDescriptionList, startDateList,
      endDateList, statusList, candidateList, mIDList;
  private final String electionType;
  private Context mContext;

  public ElectionsRecyclerAdapter(ArrayList<String> mIDList, Context context,
      ArrayList<String> electionNameList, ArrayList<String> electionDescriptionList,
      ArrayList<String> startDateList, ArrayList<String> endDateList, ArrayList<String> statusList,
      ArrayList<String> candidateList, String type) {
    this.mIDList = mIDList;
    mContext = context;
    this.electionNameList = electionNameList;
    this.electionDescriptionList = electionDescriptionList;
    this.startDateList = startDateList;
    this.endDateList = endDateList;
    this.statusList = statusList;
    this.candidateList = candidateList;
    this.electionType = type;
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
  public void onBindViewHolder(@NonNull ElectionsViewHolder holder, final int position) {
    holder.mElectionName.setText(electionNameList.get(position));
    holder.mElectionDescription.setText(electionDescriptionList.get(position));
    holder.mEndDate.setText(endDateList.get(position));
    holder.mStartDate.setText(startDateList.get(position));
    holder.mStatus.setText(statusList.get(position));
    holder.mCandidateList.setText(candidateList.get(position));
    switch (electionType) {
      case "active":
        holder.constraintLayout.setBackgroundColor(
            mContext.getResources().getColor(R.color.red_active_elections));
        break;
      case "finished":
        holder.constraintLayout.setBackgroundColor(
            mContext.getResources().getColor(R.color.blue_finished_elections));
        break;
      case "pending":
        holder.constraintLayout.setBackgroundColor(
            mContext.getResources().getColor(R.color.green_pending_elections));
        break;
      case "total":
        holder.constraintLayout.setBackgroundColor(
            mContext.getResources().getColor(R.color.orange_total_elections));
        break;
    }
    holder.electionLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(mContext, ElectionActivity.class);
        intent.putExtra("election_name", electionNameList.get(position));
        intent.putExtra("election_description", electionDescriptionList.get(position));
        intent.putExtra("start_date", startDateList.get(position));
        intent.putExtra("end_date", endDateList.get(position));
        intent.putExtra("candidates", candidateList.get(position));
        intent.putExtra("status", statusList.get(position));
        intent.putExtra("id", mIDList.get(position));
        mContext.startActivity(intent);
      }
    });
  }

  @Override
  public int getItemCount() {
    return electionNameList.size();
  }

  class ElectionsViewHolder extends RecyclerView.ViewHolder {
    final TextView mElectionName, mElectionDescription, mStartDate, mEndDate, mStatus,
        mCandidateList;
    final ConstraintLayout constraintLayout;
    final LinearLayout electionLayout;

    ElectionsViewHolder(@NonNull View itemView) {
      super(itemView);
      mElectionName = itemView.findViewById(R.id.text_view_election_name);
      mElectionDescription = itemView.findViewById(R.id.text_view_election_description);
      mStartDate = itemView.findViewById(R.id.text_view_start_date);
      mEndDate = itemView.findViewById(R.id.text_view_end_date);
      mStatus = itemView.findViewById(R.id.text_view_status);
      mCandidateList = itemView.findViewById(R.id.text_view_candidate_list);
      constraintLayout = itemView.findViewById(R.id.constraintLayout);
      electionLayout = itemView.findViewById(R.id.linearLayout);
    }
  }
}
