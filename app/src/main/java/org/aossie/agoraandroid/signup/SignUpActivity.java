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

    if (!validateUsername(userName)) {
      mUserNameEditText.setError("Please enter User Name");
    } else {
      mUserNameEditText.setError(null);
    }

    if (!validateFirstName(firstName)) {
      mFirstNameEditText.setError("Please enter First Name");
    } else {
      mFirstNameEditText.setError(null);
    }

    if (!validateLastName(lastName)) {
      mLastNameEditText.setError("Please enter Last Name");
    } else {
      mLastNameEditText.setError(null);
    }

    if (!validatePassword(userPass)) {
      mPasswordEditText.setError("Please enter password");
    } else {
      mPasswordEditText.setError(null);
    }

    if (!validateSecurityQuestionAnswer(securityQuestionAnswer)) {
      mSecurityAnswer.setError("Please enter Security Answer");
    } else {
      mSecurityAnswer.setError(null);
    }

    if (!validateEmail(userEmail)) {
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
        if (!mUserNameEditText.getEditText().getText().toString().isEmpty()) {
          mUserNameEditText.setError(null);
        } else {
          mUserNameEditText.setError("Please enter User Name");
        }

        if (!mFirstNameEditText.getEditText().getText().toString().isEmpty()) {
          mFirstNameEditText.setError(null);
        } else {
          mFirstNameEditText.setError("Please enter First Name");
        }

        if (!mLastNameEditText.getEditText().getText().toString().isEmpty()) {
          mLastNameEditText.setError(null);
        } else {
          mLastNameEditText.setError("Please enter Last Name");
        }

        if (!mPasswordEditText.getEditText().getText().toString().isEmpty()) {
          mPasswordEditText.setError(null);
        } else {
          mPasswordEditText.setError("Please enter Password");
        }

        if (!mSecurityAnswer.getEditText().getText().toString().isEmpty()) {
          mSecurityAnswer.setError(null);
        } else {
          mSecurityAnswer.setError("Please enter Security Answer");
        }

        if (!mEmailEditText.getEditText().getText().toString().isEmpty()) {
          mEmailEditText.setError(null);
        } else {
          mEmailEditText.setError("Please enter Email");
        }
      }
    }

    @Override public void afterTextChanged(Editable s) {
    }
  };

  private boolean validateUsername(String userName) {
    if (userName.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  private boolean validateFirstName(String firstName) {
    if (firstName.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  private boolean validateLastName(String lastName) {
    if (lastName.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  private boolean validateEmail(String userEmail) {
    if (userEmail.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }
  
  private boolean validatePassword(String userPass) {
    if (userPass.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }

  private boolean validateSecurityQuestionAnswer(String securityQuestionAnswer) {
    if (securityQuestionAnswer.isEmpty()) {
      return false;
    } else {
      return true;
    }
  }
}



