package org.aossie.agoraandroid.createElection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter;
import org.aossie.agoraandroid.utilities.TinyDB;

import java.util.ArrayList;

@SuppressWarnings("ConstantConditions")
public class CreateElectionTwo extends AppCompatActivity {
    private TinyDB tinydb;
    private final ArrayList<String> mCandidates = new ArrayList<>();
    private CandidateRecyclerAdapter candidateRecyclerAdapter;
    private TextInputLayout mAddCandidateTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_two);
        Button mAddCandidateButton = findViewById(R.id.button_add_candidate);
        tinydb= new TinyDB(getApplication());
        Button mNextButton = findViewById(R.id.button_next_two);
        mAddCandidateTextInput = findViewById(R.id.text_input_candidate);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view_names);

        candidateRecyclerAdapter = new CandidateRecyclerAdapter(mCandidates, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(candidateRecyclerAdapter);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCandidates != null) {
                    tinydb.putListString("Candidates",mCandidates);
                    startActivity(new Intent(CreateElectionTwo.this, CreateElectionThree.class));
                } else {
                    Toast.makeText(CreateElectionTwo.this, "Please Add At least One Candidate", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAddCandidateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mAddCandidateTextInput.getEditText().getText().toString();
                addCandidate(name);
            }
        });

    }


    private void addCandidate(String cName) {
        mCandidates.add(cName);
        candidateRecyclerAdapter.notifyDataSetChanged();
        mAddCandidateTextInput.getEditText().setText("");
    }

    private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mCandidates.remove(viewHolder.getAdapterPosition());
            candidateRecyclerAdapter.notifyDataSetChanged();

        }
    };


}
