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

public class VoterRecyclerAdapter
    extends RecyclerView.Adapter<VoterRecyclerAdapter.VoterViewHolder> {
  private final ArrayList<String> mVoterNameList;
  private final ArrayList<String> mVoterEmailList;

  public VoterRecyclerAdapter(ArrayList<String> mVoterNameList, ArrayList<String> mVoterEmailList) {
    this.mVoterNameList = mVoterNameList;
    this.mVoterEmailList = mVoterEmailList;
  }

  @NonNull
  @Override
  public VoterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater li = (LayoutInflater) parent.getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View voterView = li.inflate(R.layout.list_item_voter_details, parent, false);
    return new VoterRecyclerAdapter.VoterViewHolder(voterView);
  }

  @Override
  public void onBindViewHolder(@NonNull VoterViewHolder holder, int position) {
    holder.voterName.setText(mVoterNameList.get(position));
    holder.voterEmail.setText(mVoterEmailList.get(position));
  }

  @Override
  public int getItemCount() {
    return mVoterEmailList.size();
  }

  static class VoterViewHolder extends RecyclerView.ViewHolder {
    final TextView voterName, voterEmail;
    final View itemView;

    public VoterViewHolder(@NonNull View itemView) {
      super(itemView);
      voterName = itemView.findViewById(R.id.text_view_voter_name);
      voterEmail = itemView.findViewById(R.id.text_view_voters_email);
      this.itemView = itemView;
    }
  }
}
