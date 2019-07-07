package org.aossie.agoraandroid.createElection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.aossie.agoraandroid.R;

public class CreateElectionFour extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_four);
        Button mSubmitButton=findViewById(R.id.button_submit_details);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Calling from here
            }
        });
    }
}
