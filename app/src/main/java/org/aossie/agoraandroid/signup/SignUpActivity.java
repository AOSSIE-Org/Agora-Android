 package org.aossie.agoraandroid.signup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.login.LoginActivity;
import org.aossie.agoraandroid.utilities.HideKeyboard;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {
  private TextInputLayout mUserNameEditText, mFirstNameEditText, mLastNameEditText, mEmailEditText,
      mPasswordEditText, mSecurityAnswer,mConfirmPasswordEditText,mPhoneNumberEditText;
  private TextView mAlreadyText;
  private SignUpViewModel signUpViewModel;
  private AppCompatSpinner appCompatSpinner;
  private String securityQuestionOfSignUp;

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
    mConfirmPasswordEditText=findViewById(R.id.confirm_pass);
    mPhoneNumberEditText=findViewById(R.id.phone);
    Button mSignUpButton = findViewById(R.id.signup_btn);
 mAlreadyText = findViewById(R.id.mAlready);
    mSignUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        HideKeyboard.INSTANCE.hideKeyboardInActivity(SignUpActivity.this);
        validateAllFields();
      }
    });

    mAlreadyText.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
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
  }

  private void validateAllFields() {
    String userName = mUserNameEditText.getEditText().getText().toString();
    String firstName = mFirstNameEditText.getEditText().getText().toString();
    String lastName = mLastNameEditText.getEditText().getText().toString();
    String userEmail = mEmailEditText.getEditText().getText().toString();
    String userPass = mPasswordEditText.getEditText().getText().toString();
    String securityQuestionAnswer = mSecurityAnswer.getEditText().getText().toString();
    String userConfirmPass =mConfirmPasswordEditText.getEditText().getText().toString();
    String userPhone = mPhoneNumberEditText.getEditText().getText().toString();
    String securityQuestion = securityQuestionOfSignUp;

    validateUsername(userName);

    if (firstName.isEmpty()) {
      mFirstNameEditText.setError("Please enter First Name");
    } else {
      mFirstNameEditText.setError(null);
    }

    if (lastName.isEmpty()) {
      mLastNameEditText.setError("Please enter Last Name");
    } else {
      mLastNameEditText.setError(null);
    }

    if (userConfirmPass.isEmpty()) {
      mConfirmPasswordEditText.setError("Please enter password");
    } else {
      mConfirmPasswordEditText.setError(null);
    }

    if (userPhone.isEmpty()) {
      mPhoneNumberEditText.setError("Please enter Phone Number");
    } else {
      mConfirmPasswordEditText.setError(null);
    }
    if (userEmail.isEmpty()) {
      mEmailEditText.setError("Please enter Email Address");
    } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
      mEmailEditText.setError("Enter a valid email address!!!");
    } else {
      mEmailEditText.setError(null);
    }

    if (securityQuestionAnswer.isEmpty()) {
      mSecurityAnswer.setError("Please enter Security Answer");
    } else {
      mSecurityAnswer.setError(null);
    }

    validatePassword(userName, firstName, lastName, userEmail,userConfirmPass,userPhone, userPass, securityQuestionAnswer,
        securityQuestion);
  }

  private void validatePassword(String userName, String firstName, String lastName,
      String userEmail,String userConfirmPass,String userPhone, String userPass, String securityQuestionAnswer, String securityQuestion) {
    if (userPass.isEmpty()) {
      mPasswordEditText.setError("Please enter password");
      if (userPass != userConfirmPass)
      {
        mPasswordEditText.setError("Password doesn't match");
        mConfirmPasswordEditText.setError("Password doesn't match");
      }
    } else {
      mPasswordEditText.setError(null);
      signUpViewModel.signUpRequest(userName, userPass,userConfirmPass, userPhone,userEmail, firstName, lastName,
          securityQuestion, securityQuestionAnswer);
    }
  }

  private void validateUsername(String userName) {
    if (userName.isEmpty()) {
      mUserNameEditText.setError("Please enter User Name");
    } else {
      mUserNameEditText.setError(null);
    }
  }
}



