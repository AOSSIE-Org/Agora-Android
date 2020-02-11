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

public class CandidateRecyclerAdapter
    extends RecyclerView.Adapter<CandidateRecyclerAdapter.CandidateViewHolder> {
  private final ArrayList<String> candidates;

  public CandidateRecyclerAdapter(ArrayList<String> candidates) {
    this.candidates = candidates;
  }

  @NonNull
  @Override
  public CandidateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater li = (LayoutInflater) parent.getContext()
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View candidateView = li.inflate(R.layout.list_item_candidate_name, parent, false);
    return new CandidateViewHolder(candidateView);
  }

  @Override
  public void onBindViewHolder(@NonNull CandidateViewHolder holder, int position) {
    holder.candidateName.setText(candidates.get(position));
  }

  @Override
  public int getItemCount() {
    return candidates.size();
  }

  static class CandidateViewHolder extends RecyclerView.ViewHolder {
    final TextView candidateName;
    final View itemView;

    public CandidateViewHolder(@NonNull View itemView) {
      super(itemView);
      candidateName = itemView.findViewById(R.id.text_candidate_name);
      this.itemView = itemView;
    }
  }
}
