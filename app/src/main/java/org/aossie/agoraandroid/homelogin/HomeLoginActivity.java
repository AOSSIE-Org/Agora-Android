package org.aossie.agoraandroid.homelogin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import java.util.Arrays;
import org.aossie.agoraandroid.R;
import org.aossie.agoraandroid.login.LoginActivity;
import org.aossie.agoraandroid.login.LoginViewModel;
import org.aossie.agoraandroid.signup.SignUpActivity;

public class HomeLoginActivity extends AppCompatActivity {

  private CallbackManager callbackManager;
  private LoginViewModel loginViewModel;
  AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
    @Override
    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
        AccessToken currentAccessToken) {
      if (currentAccessToken == null) {
        Toast.makeText(HomeLoginActivity.this, "User Logged Out", Toast.LENGTH_SHORT).show();
      } else {
        String facebookAccessToken = currentAccessToken.getToken();
        loginViewModel.facebookLogInRequest(facebookAccessToken);
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_login);
    Button signIn = findViewById(R.id.signin_btn);
    Button signUp = findViewById(R.id.signup_btn);
    loginViewModel = new LoginViewModel(getApplication(), this);
    FacebookSdk.sdkInitialize(this.getApplicationContext());

    callbackManager = CallbackManager.Factory.create();
    Button facebookLogin = findViewById(R.id.fb_login_btn);

    LoginManager.getInstance().registerCallback(callbackManager,
        new FacebookCallback<LoginResult>() {
          @Override
          public void onSuccess(LoginResult loginResult) {
            Log.d("Success", "Login");
          }

          @Override
          public void onCancel() {
            Toast.makeText(HomeLoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
          }

          @Override
          public void onError(FacebookException exception) {
            Toast.makeText(HomeLoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG)
                .show();
          }
        });

    facebookLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        LoginManager.getInstance()
            .logInWithReadPermissions(HomeLoginActivity.this,
                Arrays.asList("public_profile", "user_friends"));
      }
    });
    signIn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent logInIntent = new Intent(HomeLoginActivity.this, LoginActivity.class);
        logInIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logInIntent);
      }
    });
    signUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent signUpIntent = new Intent(HomeLoginActivity.this, SignUpActivity.class);
        startActivity(signUpIntent);
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    callbackManager.onActivityResult(requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }
}
