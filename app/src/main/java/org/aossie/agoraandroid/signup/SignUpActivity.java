package org.aossie.agoraandroid.signup;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

  private boolean userNameValidated = false;
  private boolean firstNameValidated = false;
  private boolean lastNameValidated = false;
  private boolean emailValidated = false;
  private boolean passwordValidated = false;
  private boolean securityAnswerValidated = false;

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
    validateUserEmail(userEmail);

    if (userNameValidated && firstNameValidated && lastNameValidated
        && emailValidated && passwordValidated && securityAnswerValidated) {
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
      mUserNameEditText.setError(getString(R.string.user_name_empty));
      userNameValidated = false;
    } else {
      mUserNameEditText.setError(null);
      userNameValidated = true;
    }
  }

  private void validateFirstName(String firstName) {
    if (firstName.isEmpty()) {
      mFirstNameEditText.setError(getString(R.string.first_name_empty));
      firstNameValidated = false;
    } else {
      mFirstNameEditText.setError(null);
      firstNameValidated = true;
    }
  }

  private void validateLastName(String lastName) {
    if (lastName.isEmpty()) {
      mLastNameEditText.setError(getString(R.string.last_name_empty));
      lastNameValidated = false;
    } else {
      mLastNameEditText.setError(null);
      lastNameValidated = true;
    }
  }

  private void validateUserPass(String userPass) {
    if (userPass.isEmpty()) {
      mPasswordEditText.setError(getString(R.string.password_empty_warn));
      passwordValidated = false;
    } else {
      mPasswordEditText.setError(null);
      passwordValidated = true;
    }
  }

  private void validateSecurityAnswer(String securityQuestionAnswer) {
    if (securityQuestionAnswer.isEmpty()) {
      mSecurityAnswer.setError(getString(R.string.security_answer_empty));
      securityAnswerValidated = false;
    } else {
      mSecurityAnswer.setError(null);
      securityAnswerValidated = true;
    }
  }

  private void validateUserEmail(String userEmail) {
    if (userEmail.isEmpty()) {
      mEmailEditText.setError(getString(R.string.email_empty));
      emailValidated = false;
    } else if (!Patterns.EMAIL_ADDRESS
        .matcher(mEmailEditText.getEditText().getText().toString()).matches()) {
      mEmailEditText.setError(getString(R.string.invalid_email));
      emailValidated = false;
    } else {
      mEmailEditText.setError(null);
      emailValidated = true;
    }
  }
}



