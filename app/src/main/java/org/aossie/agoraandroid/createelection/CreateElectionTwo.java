package org.aossie.agoraandroid.createelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.CandidateRecyclerAdapter;
import org.aossie.agoraandroid.utilities.TinyDB;

@SuppressWarnings("ConstantConditions")
public class CreateElectionTwo extends AppCompatActivity {
  private final ArrayList<String> mCandidates = new ArrayList<>();
  private TinyDB tinydb;
  private CandidateRecyclerAdapter candidateRecyclerAdapter;
  private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
      new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
          return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
          mCandidates.remove(viewHolder.getAdapterPosition());
          candidateRecyclerAdapter.notifyDataSetChanged();
        }
      };
  private TextInputLayout mAddCandidateTextInput;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_election_two);
    Button mAddCandidateButton = findViewById(R.id.add_candidate_btn);
    tinydb = new TinyDB(getApplication());
    Button mNextButton = findViewById(R.id.submit_details_btn);
    mAddCandidateTextInput = findViewById(R.id.candidate_til);
    RecyclerView mRecyclerView = findViewById(R.id.names_rv);

    candidateRecyclerAdapter = new CandidateRecyclerAdapter(mCandidates);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    mRecyclerView.setAdapter(candidateRecyclerAdapter);
    mNextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mCandidates.size() != 0) {
          tinydb.putListString("Candidates", mCandidates);
          startActivity(new Intent(CreateElectionTwo.this, CreateElectionThree.class));
        } else {
          Toast.makeText(CreateElectionTwo.this, "Please Add At least One Candidate",
              Toast.LENGTH_SHORT).show();
        }
      }
    });
    mAddCandidateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String name = mAddCandidateTextInput.getEditText().getText().toString().trim();
        addCandidate(name);
      }
    });
  }

  private void addCandidate(String cName) {
    mCandidates.add(cName);
    candidateRecyclerAdapter.notifyDataSetChanged();
    mAddCandidateTextInput.getEditText().setText("");
  }
}
