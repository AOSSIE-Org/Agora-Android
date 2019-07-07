package org.aossie.agoraandroid.createElection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter;

import java.util.ArrayList;

public class CreateElectionTwo extends AppCompatActivity {
    ArrayList<String> candidates = new ArrayList<>();
    Button mAddCandidateButton, mNextButton;
    CandidateRecyclerAdapter candidateRecyclerAdapter;
    TextInputLayout mAddCandidateTextInput;
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_two);
        mAddCandidateButton = findViewById(R.id.button_add_candidate);
        mNextButton = findViewById(R.id.button_next_one);
        mAddCandidateTextInput = findViewById(R.id.text_input_candidate);
        mRecyclerView = findViewById(R.id.recycler_view_names);

        candidateRecyclerAdapter = new CandidateRecyclerAdapter(candidates, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(candidateRecyclerAdapter);

        mAddCandidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mAddCandidateTextInput.getEditText().getText().toString();
                addCandidate(name);
            }
        });

    }


    public void addCandidate(String cName) {
        candidates.add(cName);
        candidateRecyclerAdapter.notifyDataSetChanged();
        mAddCandidateTextInput.getEditText().setText("");
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            candidates.remove(viewHolder.getAdapterPosition());
            candidateRecyclerAdapter.notifyDataSetChanged();

        }
    };

}
