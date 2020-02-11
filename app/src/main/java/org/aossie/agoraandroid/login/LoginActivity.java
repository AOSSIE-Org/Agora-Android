package org.aossie.agoraandroid.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputLayout;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.forgotpassword.ForgotPasswordSend;

@SuppressWarnings("ConstantConditions")
public class LoginActivity extends AppCompatActivity {
  private TextInputLayout mLoginUserName, mLoginPassword;
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
    mForgotPassword.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordSend.class));
      }
    });

    mFinalLoginButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final String userName = mLoginUserName.getEditText().getText().toString().trim();
        final String userPass = mLoginPassword.getEditText().getText().toString().trim();

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
}