package org.aossie.agoraandroid.invitevoters;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import java.util.ArrayList;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.adapters.TextWatcherAdapter;
import org.aossie.agoraandroid.adapters.VoterRecyclerAdapter;
import org.json.JSONException;

public class InviteVotersActivity extends AppCompatActivity {
  private final ArrayList<String> mVoterNames = new ArrayList<>();
  private final ArrayList<String> mVoterEmails = new ArrayList<>();
  private InviteVotersViewModel inviteVotersViewModel;
  private VoterRecyclerAdapter voterRecyclerAdapter;
  private TextWatcherAdapter textWatcherAdapter;
  private final ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
      new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
          return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
          mVoterNames.remove(viewHolder.getAdapterPosition());
          mVoterEmails.remove(viewHolder.getAdapterPosition());
          voterRecyclerAdapter.notifyDataSetChanged();
        }
      };
  private TextInputLayout mVoterNameTextInput, mVoterEmailTextInput;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_invite_voters);
    inviteVotersViewModel = new InviteVotersViewModel(getApplication(), this);

    mVoterNameTextInput = findViewById(R.id.text_input_voter_name);
    mVoterEmailTextInput = findViewById(R.id.text_input_voter_email);
    Button mAddVoters = findViewById(R.id.button_add_voter);
    final Button mInviteVotes = findViewById(R.id.button_invite_voter);
    RecyclerView mRecyclerView = findViewById(R.id.recycler_view_voters);
    textWatcherAdapter  = new TextWatcherAdapter(this);
    voterRecyclerAdapter = new VoterRecyclerAdapter(mVoterNames, mVoterEmails);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
    mRecyclerView.setAdapter(voterRecyclerAdapter);

    mVoterNameTextInput.getEditText().addTextChangedListener(textWatcherAdapter);
    mVoterEmailTextInput.getEditText().addTextChangedListener(textWatcherAdapter);

    mInviteVotes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          if (getIntent().hasExtra("id") && getIntent().hasExtra("token")) {
            String id = getIntent().getStringExtra("id");
            String token = getIntent().getStringExtra("token");
            inviteVotersViewModel.inviteVoters(mVoterNames, mVoterEmails, id, token);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    });

    mAddVoters.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String name = mVoterNameTextInput.getEditText().getText().toString();
        final String email = mVoterEmailTextInput.getEditText().getText().toString();

        if(inviteVotersViewModel.inviteValidator(email,name,mVoterEmails))
        {
          addCandidate(name, email);
        }
      }
    });
  }

  private void addCandidate(String voterName, String voterEmail) {
    mVoterNames.add(voterName);
    mVoterEmails.add(voterEmail);
    voterRecyclerAdapter.notifyDataSetChanged();
    mVoterNameTextInput.getEditText().setText("");
    mVoterEmailTextInput.getEditText().setText("");
  }
}
