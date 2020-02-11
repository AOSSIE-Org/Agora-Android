package org.aossie.agoraandroid.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import org.aossie.agoraandroid.R;

public class BallotRecyclerAdapter
    extends RecyclerView.Adapter<BallotRecyclerAdapter.BallotViewHolder> {
  private final ArrayList<String> mVoteBallotList, mVoterEmailList;

  public BallotRecyclerAdapter(ArrayList<String> mVoterEmailList,
      ArrayList<String> mVoteBallotList) {
    this.mVoteBallotList = mVoteBallotList;
    this.mVoterEmailList = mVoterEmailList;
  }

  @NonNull
  @Override
  public BallotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View itemView = layoutInflater.inflate(R.layout.list_item_ballot_details, parent, false);

    return new BallotRecyclerAdapter.BallotViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull BallotViewHolder holder, int position) {
    holder.mVoterEmail.setText(mVoterEmailList.get(position));
    holder.mVoterBallot.setText(mVoteBallotList.get(position));
  }

  @Override
  public int getItemCount() {
    return mVoteBallotList.size();
  }

  class BallotViewHolder extends RecyclerView.ViewHolder {
    final TextView mVoterBallot, mVoterEmail;

    BallotViewHolder(@NonNull View itemView) {
      super(itemView);
      mVoterBallot = itemView.findViewById(R.id.text_view_vote_ballot);
      mVoterEmail = itemView.findViewById(R.id.text_view_voter_email);
    }
  }
}
