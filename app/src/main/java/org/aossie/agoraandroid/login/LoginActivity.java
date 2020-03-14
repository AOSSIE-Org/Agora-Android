package org.aossie.agoraandroid.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.forgotpassword.ForgotPasswordSend;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity {
  private TextInputLayout mLoginUserName, mLoginPassword;
  private TextInputEditText edtUsername, edtPassword;
  private LoginViewModel loginViewModel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginViewModel = new LoginViewModel(getApplication(), this);

    mLoginPassword = findViewById(R.id.login_password_til);
    mLoginUserName = findViewById(R.id.login_user_name_til);
    Button mFinalLoginButton = findViewById(R.id.login_btn);
    TextView mForgotPassword = findViewById(R.id.forgot_password_tv);
    edtUsername = findViewById(R.id.user_name_tiet);
    edtPassword = findViewById(R.id.password_tite);
    mForgotPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordSend.class));
      }
    });




    edtUsername.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mLoginUserName.setError(null);
        return false;
      }
    });

    edtPassword.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View view, MotionEvent motionEvent) {
        mLoginPassword.setError(null);
        return false;
      }
    });

    mFinalLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String userName = mLoginUserName.getEditText().getText().toString().trim();
        final String userPass = mLoginPassword.getEditText().getText().toString().trim();

        hideKeyboard(LoginActivity.this);

        if (userName.isEmpty()) {
          mLoginUserName.setError("Please enter User Name");
        } else {
          mLoginUserName.setError(null);
        }

        if (userPass.isEmpty()) {
          mLoginPassword.setError("Please enter password");
        } else {
          mLoginPassword.setError(null);
          loginViewModel.logInRequest(userName, userPass);
        }
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