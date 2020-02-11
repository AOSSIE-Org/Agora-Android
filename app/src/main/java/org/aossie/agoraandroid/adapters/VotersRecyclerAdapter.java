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

public class VotersRecyclerAdapter
    extends RecyclerView.Adapter<VotersRecyclerAdapter.VoterViewHolder> {
  private final ArrayList<String> mVoteNameList, mVoterEmailList;

  public VotersRecyclerAdapter(ArrayList<String> mVoteNameList, ArrayList<String> mVoterEmailList) {
    this.mVoteNameList = mVoteNameList;
    this.mVoterEmailList = mVoterEmailList;
  }

  @NonNull
  @Override
  public VoterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View itemView = layoutInflater.inflate(R.layout.list_item_voter_details, parent, false);

    return new VotersRecyclerAdapter.VoterViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull VoterViewHolder holder, int position) {
    holder.mVoterEmail.setText(mVoterEmailList.get(position));
    holder.mVoterName.setText(mVoteNameList.get(position));
  }

  @Override
  public int getItemCount() {
    return mVoteNameList.size();
  }

  class VoterViewHolder extends RecyclerView.ViewHolder {
    final TextView mVoterName, mVoterEmail;

    VoterViewHolder(@NonNull View itemView) {
      super(itemView);
      mVoterName = itemView.findViewById(R.id.text_view_voter_name);
      mVoterEmail = itemView.findViewById(R.id.text_view_voters_email);
    }
  }
}
