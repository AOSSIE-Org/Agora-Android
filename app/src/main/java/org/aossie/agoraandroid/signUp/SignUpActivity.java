package org.aossie.agoraandroid.signUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.logIn.LogInActivity;

public class SignUpActivity extends AppCompatActivity {
    private TextInputLayout mUserNameEditText, mFirstNameEditText, mLastNameEditText, mEmailEditText, mPasswordEditText;
    private SignUpViewModel signUpViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpViewModel = new SignUpViewModel(getApplication(),this);


        mUserNameEditText = findViewById(R.id.signUpUserName);
        mFirstNameEditText = findViewById(R.id.signUpFirstName);
        mLastNameEditText = findViewById(R.id.signUpLastName);
        mEmailEditText = findViewById(R.id.signUpEmail);
        mPasswordEditText = findViewById(R.id.signUpPassword);
        TextView signIn=findViewById(R.id.signIn);
        Button mSignUpButton = findViewById(R.id.signUpButton);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userName = mUserNameEditText.getEditText().getText().toString();
                final String firstName = mFirstNameEditText.getEditText().getText().toString();
                final String lastName = mLastNameEditText.getEditText().getText().toString();
                final String userEmail = mEmailEditText.getEditText().getText().toString();
                final String userPass = mPasswordEditText.getEditText().getText().toString();

                if (userName.isEmpty())
                    mUserNameEditText.setError("Please enter User Name");
                else mUserNameEditText.setError(null);

                if (firstName.isEmpty()) {
                    mFirstNameEditText.setError("Please enter First Name");
                } else mFirstNameEditText.setError(null);

                if (lastName.isEmpty()) {
                    mLastNameEditText.setError("Please enter Last Name");
                } else mLastNameEditText.setError(null);

                if (userEmail.isEmpty()) {
                    mEmailEditText.setError("PLease enter Email Address");
                } else mEmailEditText.setError(null);

                if (userPass.isEmpty()) {
                    mPasswordEditText.setError("Please enter password");
                } else {
                    mPasswordEditText.setError(null);
                    signUpViewModel.signUpRequest(userName, userPass, userEmail, firstName, lastName);
                }

            }
        });


    }


    }



