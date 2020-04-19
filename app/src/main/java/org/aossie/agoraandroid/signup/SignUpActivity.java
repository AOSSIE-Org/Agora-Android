package org.aossie.agoraandroid.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.utilities.HideKeyboard;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {
  private TextInputLayout mUserNameEditText, mFirstNameEditText, mLastNameEditText, mEmailEditText,
      mPasswordEditText, mSecurityAnswer;
  private SignUpViewModel signUpViewModel;
  private AppCompatSpinner appCompatSpinner;
  private String securityQuestionOfSignUp;
  private boolean switchOnWatcher = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_signup);

    signUpViewModel = new SignUpViewModel(getApplication(), this);
    mUserNameEditText = findViewById(R.id.signup_user_name);
    mFirstNameEditText = findViewById(R.id.signup_first_name);
    mLastNameEditText = findViewById(R.id.signup_last_name);
    mEmailEditText = findViewById(R.id.signup_email);
    mPasswordEditText = findViewById(R.id.signup_password);
    appCompatSpinner = findViewById(R.id.sign_up_security_question);
    mSecurityAnswer = findViewById(R.id.security_answer);
    Button mSignUpButton = findViewById(R.id.signup_btn);

    mSignUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //to enable the textwatcher checking after the first click
        switchOnWatcher = true;
        HideKeyboard.INSTANCE.hideKeyboardInActivity(SignUpActivity.this);
        validateAllFields();
      }
    });

    final ArrayAdapter<CharSequence> adapter =
        ArrayAdapter.createFromResource(this, R.array.security_questions,
            android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    appCompatSpinner.setAdapter(adapter);
    appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        securityQuestionOfSignUp = adapterView.getItemAtPosition(i).toString();
      }

      @Override
      public void onNothingSelected(AdapterView<?> adapterView) {
        securityQuestionOfSignUp = getResources().getStringArray(R.array.security_questions)[0];
      }
    });

    mUserNameEditText.getEditText().addTextChangedListener(signUpWatcher);
    mFirstNameEditText.getEditText().addTextChangedListener(signUpWatcher);
    mLastNameEditText.getEditText().addTextChangedListener(signUpWatcher);
    mPasswordEditText.getEditText().addTextChangedListener(signUpWatcher);
    mSecurityAnswer.getEditText().addTextChangedListener(signUpWatcher);
    mEmailEditText.getEditText().addTextChangedListener(signUpWatcher);
  }

  private void validateAllFields() {
    String userName = mUserNameEditText.getEditText().getText().toString();
    String firstName = mFirstNameEditText.getEditText().getText().toString();
    String lastName = mLastNameEditText.getEditText().getText().toString();
    String userEmail = mEmailEditText.getEditText().getText().toString();
    String userPass = mPasswordEditText.getEditText().getText().toString();
    String securityQuestionAnswer = mSecurityAnswer.getEditText().getText().toString();
    String securityQuestion = securityQuestionOfSignUp;

    validateUsername(userName);
    validateFirstName(firstName);
    validateLastName(lastName);
    validateUserPass(userPass);
    validateSecurityAnswer(securityQuestionAnswer);

    if (userEmail.isEmpty()) {
      mEmailEditText.setError("Please enter Email Address");
    } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
      mEmailEditText.setError("Enter a valid email address!!!");
    } else {
      mEmailEditText.setError(null);
      signUpViewModel.signUpRequest(userName, userPass, userEmail, firstName, lastName,
          securityQuestion, securityQuestionAnswer);
    }
  }

  private TextWatcher signUpWatcher = new TextWatcher() {
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
      if (switchOnWatcher) {
        validateUsername(mUserNameEditText.getEditText().getText().toString());
        validateFirstName(mFirstNameEditText.getEditText().getText().toString());
        validateLastName(mLastNameEditText.getEditText().getText().toString());
        validateUserPass(mPasswordEditText.getEditText().getText().toString());
        validateSecurityAnswer(mSecurityAnswer.getEditText().getText().toString());
        validateUserEmail(mEmailEditText.getEditText().getText().toString());
      }
    }

    @Override public void afterTextChanged(Editable s) {
    }
  };

  private void validateUsername(String userName) {
    if (userName.isEmpty()) {
      mUserNameEditText.setError("Please enter User Name");
    } else {
      mUserNameEditText.setError(null);
    }
  }

  private void validateFirstName(String firstName) {
    if (firstName.isEmpty()) {
      mFirstNameEditText.setError("Please enter First Name");
    } else {
      mFirstNameEditText.setError(null);
    }
  }

  private void validateLastName(String lastName) {
    if (lastName.isEmpty()) {
      mLastNameEditText.setError("Please enter Last Name");
    } else {
      mLastNameEditText.setError(null);
    }
  }

  private void validateUserPass(String userPass) {
    if (userPass.isEmpty()) {
      mPasswordEditText.setError("Please enter password");
    } else {
      mPasswordEditText.setError(null);
    }
  }

  private void validateSecurityAnswer(String securityQuestionAnswer) {
    if (securityQuestionAnswer.isEmpty()) {
      mSecurityAnswer.setError("Please enter Security Answer");
    } else {
      mSecurityAnswer.setError(null);
    }
  }

  private void validateUserEmail(String userEmail) {
    if (userEmail.isEmpty()) {
      mEmailEditText.setError("Please enter email address");
    } else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getEditText().getText().toString()).matches()) {
      mEmailEditText.setError("A valid email address is required!!!");
    } else {
      mEmailEditText.setError(null);
    }
  }
}



