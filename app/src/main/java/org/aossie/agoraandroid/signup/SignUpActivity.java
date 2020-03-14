package org.aossie.agoraandroid.signup;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;

@SuppressWarnings("ConstantConditions")
public class SignUpActivity extends AppCompatActivity {
  private TextInputLayout mUserNameEditText, mFirstNameEditText, mLastNameEditText, mEmailEditText,
      mPasswordEditText, mSecurityAnswer;
  private TextInputEditText edtUsername, edtPassword, edtFirst_name, edtLast_name, edtEmail,
      edtTextPassword, edtQues;
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
    Button mSignUpButton = findViewById(R.id.signup_btn);
    edtUsername = findViewById(R.id.edt_user_name);
    edtEmail = findViewById(R.id.edt_email);
    edtFirst_name = findViewById(R.id.edt_first_name);
    edtLast_name = findViewById(R.id.edt_last_name);
    edtTextPassword = findViewById(R.id.edt_password);
    edtQues = findViewById(R.id.edt_ques);

    edtUsername.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mUserNameEditText.setError(null);
        return false;
      }
    });

    edtEmail.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mEmailEditText.setError(null);
        return false;
      }
    });

    edtFirst_name.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mFirstNameEditText.setError(null);
        return false;
      }
    });

    edtLast_name.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mLastNameEditText.setError(null);
        return false;
      }
    });

    edtTextPassword.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mPasswordEditText.setError(null);
        return false;
      }
    });

    edtQues.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mSecurityAnswer.setError(null);
        return false;
      }
    });

    mSignUpButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String userName = mUserNameEditText.getEditText().getText().toString();
        String firstName = mFirstNameEditText.getEditText().getText().toString();
        String lastName = mLastNameEditText.getEditText().getText().toString();
        String userEmail = mEmailEditText.getEditText().getText().toString();
        String userPass = mPasswordEditText.getEditText().getText().toString();
        String securityQuestionAnswer = mSecurityAnswer.getEditText().getText().toString();
        String securityQuestion = securityQuestionOfSignUp;

        hideKeyboard(SignUpActivity.this);

        if (userName.isEmpty()) {
          mUserNameEditText.setError("Please enter User Name");
        } else {
          mUserNameEditText.setError(null);
        }

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

        if (userEmail.isEmpty()) {
          mEmailEditText.setError("Please enter Email Address");
        } else {
          mEmailEditText.setError(null);
        }

        if (securityQuestionAnswer.isEmpty()) {
          mSecurityAnswer.setError("Please enter Security Answer");
        } else {
          mSecurityAnswer.setError(null);
        }

        if (userPass.isEmpty()) {
          mPasswordEditText.setError("Please enter password");
        } else {
          mPasswordEditText.setError(null);
          signUpViewModel.signUpRequest(userName, userPass, userEmail, firstName, lastName,
              securityQuestion, securityQuestionAnswer);
        }
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

  public static void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    View view = activity.getCurrentFocus();
    if (view == null) {
      view = new View(activity);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }
}



