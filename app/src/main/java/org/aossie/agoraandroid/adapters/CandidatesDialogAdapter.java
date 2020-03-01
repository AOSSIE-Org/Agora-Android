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

public class CandidatesDialogAdapter extends RecyclerView.Adapter<CandidatesDialogAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> candidatesList;

    public CandidatesDialogAdapter(Context context, ArrayList<String> candidatesList) {
        this.context = context;
        this.candidatesList = candidatesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_candidates, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvCandidates.setText(candidatesList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return candidatesList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCandidates;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCandidates = itemView.findViewById(R.id.tv_candidate);
        }
    }
}
