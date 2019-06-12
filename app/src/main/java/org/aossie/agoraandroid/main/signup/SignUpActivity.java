package org.aossie.agoraandroid.main.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;


import org.aossie.agoraandroid.R;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout mUserNameEditText, mFirstNameEditText, mLastNameEditText, mEmailEditText, mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        mUserNameEditText = findViewById(R.id.signUpUserName);
        mFirstNameEditText = findViewById(R.id.signUpFirstName);
        mLastNameEditText = findViewById(R.id.signUpLastName);
        mEmailEditText = findViewById(R.id.signUpEmail);
        mPasswordEditText = findViewById(R.id.signUpPassword);
        Button mSignUpButton = findViewById(R.id.signUpButton);


        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUserNameEditText.getEditText().getText().toString().isEmpty())
                    mUserNameEditText.setError("Please enter User Name");
                else mUserNameEditText.setError(null);

                if (mFirstNameEditText.getEditText().getText().toString().isEmpty()) {
                    mFirstNameEditText.setError("Please enter First Name");
                } else mFirstNameEditText.setError(null);

                if (mLastNameEditText.getEditText().getText().toString().isEmpty()) {
                    mLastNameEditText.setError("Please enter Last Name");
                } else mFirstNameEditText.setError(null);

                if (mEmailEditText.getEditText().getText().toString().isEmpty()) {
                    mEmailEditText.setError("PLease enter Email Address");
                } else mEmailEditText.setError(null);mPasswordEditText.setError(null);

                if (mPasswordEditText.getEditText().getText().toString().isEmpty()) {
                    mPasswordEditText.setError("Please enter password");
                } else {
                    mPasswordEditText.setError(null);
                    doSignUp();
                }
            }
        });


    }

    private void doSignUp() {
        String s = mUserNameEditText.getEditText().getText().toString();
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

    }


}


